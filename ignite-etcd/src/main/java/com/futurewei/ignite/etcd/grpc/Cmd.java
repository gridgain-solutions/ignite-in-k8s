package com.futurewei.ignite.etcd.grpc;

import com.futurewei.ignite.etcd.CacheConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.services.HealthStatusManager;
import org.apache.commons.cli.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Cmd {
    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
        final String PORT_OPT = "p";
        final String IGNITE_CFG_OPT = "i";
        final String KV_CACHE_OPT = "c";
        final String KV_HIST_CACHE_OPT = "h";
        final String LEASE_CACHE_OPT = "l";
        final String GRPC_KA_INTERVAL_OPT = "gpi";
        final String GRPC_KA_TIMEOUT_OPT = "gpt";
        final String GRPC_KA_MIN_TIME_OPT = "gpm";
        final String GRPC_KA_WO_STREAM_OPT = "gpa";
        final String HELP_OPT = "h";
        final int DFLT_PORT = 2379;
        final String DFLT_KV_CACHE = "etcd_kv";
        final String DFLT_KV_HIST_CACHE = "etcd_kv_history";
        final String DFLT_LEASE_CACHE = "etcd_lease";
        final long DFLT_GRPC_KA_INTERVAL = TimeUnit.HOURS.toSeconds(2);
        final long DFLT_GRPC_KA_TIMEOUT = 20;
        final long DFLT_GRPC_KA_MIN_TIME = 5;

        Options options = new Options()
            .addOption(PORT_OPT, "server-port", true, "server port (default " + DFLT_PORT + ")")
            .addOption(
                IGNITE_CFG_OPT,
                "ignite-config",
                true,
                "ignite configuration file (default configuration is used if not specified)"
            )
            .addOption(
                KV_CACHE_OPT,
                "ignite-cache-kv",
                true,
                "ignite KV cache name (default \"" + DFLT_KV_CACHE + "\"). Already defined cache must conform to " +
                    "this specification:\n" + CacheConfig.KVSpec
            )
            .addOption(
                KV_HIST_CACHE_OPT,
                "ignite-cache-kv-history",
                true,
                "ignite KV cache name (default \"" + DFLT_KV_CACHE + "\"). Already defined cache must conform to " +
                    "this specification:\n" + CacheConfig.KVHistorySpec
            )
            .addOption(
                LEASE_CACHE_OPT,
                "ignite-cache-lease",
                true,
                "ignite Lease cache name (default \"" + DFLT_LEASE_CACHE + "\"). Already defined cache must conform " +
                    " to this specification:\n" + CacheConfig.LeaseSpec
            )
            .addOption(
                GRPC_KA_INTERVAL_OPT,
                "grpc-keepalive-interval",
                true,
                "Frequency duration in seconds of server-to-client ping to check if a connection is alive " +
                    "(default 2h). 0 disables the server-to-client pings."
            )
            .addOption(
                GRPC_KA_TIMEOUT_OPT,
                "grpc-keepalive-timeout",
                true,
                "Additional duration in seconds of wait before closing a non-responsive connection (default 20s). " +
                    "0 disables closing non-responsive connections."
            )
            .addOption(
                GRPC_KA_MIN_TIME_OPT,
                "grpc-keepalive-min-time",
                true,
                "Minimum duration interval in seconds that a client should wait before pinging server (default 5s)"
            )
            .addOption(
                GRPC_KA_WO_STREAM_OPT,
                "grpc-keepalive-without-stream",
                false,
                "Allows clients to ping the server without any active streams (denied by default)."
            )
            .addOption(HELP_OPT, "help", false, "print usage");
        CommandLine cmd = new DefaultParser().parse(options, args);

        if (cmd.hasOption(HELP_OPT)) {
            new HelpFormatter().printHelp(110, "ignite-etcd", null, options, null);
            System.exit(0);
        }

        int srvPort = cmd.hasOption(PORT_OPT) ? Integer.parseInt(cmd.getOptionValue(PORT_OPT)) : DFLT_PORT;
        Ignite ignite = cmd.hasOption(IGNITE_CFG_OPT)
            ? Ignition.start(cmd.getOptionValue(IGNITE_CFG_OPT))
            : Ignition.start();
        String kvCacheName = cmd.hasOption(KV_CACHE_OPT) ? cmd.getOptionValue(KV_CACHE_OPT) : DFLT_KV_CACHE;
        String kvHistCacheName = cmd.hasOption(KV_HIST_CACHE_OPT)
            ? cmd.getOptionValue(KV_HIST_CACHE_OPT)
            : DFLT_KV_HIST_CACHE;
        String leaseCacheName = cmd.hasOption(LEASE_CACHE_OPT) ? cmd.getOptionValue(LEASE_CACHE_OPT) : DFLT_LEASE_CACHE;

        HealthStatusManager health = new HealthStatusManager();

        ServerBuilder<?> srvBuilder = ServerBuilder.forPort(srvPort)
            .addService(new Auth(ignite))
            .addService(new Cluster(ignite))
            .addService(new KV(ignite, kvCacheName, kvHistCacheName, leaseCacheName))
            .addService(new Lease(ignite, leaseCacheName, kvCacheName, kvHistCacheName))
            .addService(new Maintenance(ignite))
            .addService(new Watch(ignite, kvCacheName))
            .addService(ProtoReflectionService.newInstance())
            .addService(health.getHealthService());

        if (srvBuilder instanceof NettyServerBuilder) {
            long keepAliveTime = cmd.hasOption(GRPC_KA_INTERVAL_OPT)
                ? Long.parseLong(GRPC_KA_INTERVAL_OPT)
                : DFLT_GRPC_KA_INTERVAL;
            long keepAliveTimeout = cmd.hasOption(GRPC_KA_TIMEOUT_OPT)
                ? Long.parseLong(GRPC_KA_TIMEOUT_OPT)
                : DFLT_GRPC_KA_TIMEOUT;
            long permitKeepAliveTime = cmd.hasOption(GRPC_KA_MIN_TIME_OPT)
                ? Long.parseLong(GRPC_KA_MIN_TIME_OPT)
                : DFLT_GRPC_KA_MIN_TIME;

            ((NettyServerBuilder)srvBuilder)
                .keepAliveTime(keepAliveTime, TimeUnit.SECONDS)
                .keepAliveTimeout(keepAliveTimeout, TimeUnit.SECONDS)
                .permitKeepAliveTime(permitKeepAliveTime, TimeUnit.SECONDS)
                .permitKeepAliveWithoutCalls(cmd.hasOption(GRPC_KA_WO_STREAM_OPT));
        } else {
            System.out.println("\nWARNING! Settings " + String.join(", ", GRPC_KA_INTERVAL_OPT, GRPC_KA_TIMEOUT_OPT,
                GRPC_KA_MIN_TIME_OPT, GRPC_KA_WO_STREAM_OPT) + " are not supported for " +
                srvBuilder.getClass().getName() + ". Use netty as GRPC server to enable these settings.\n");
        }

        final Server srv = srvBuilder.build().start();

        System.out.println("Listening on port " + srvPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            srv.shutdown();
            try {
                if (!srv.awaitTermination(30, TimeUnit.SECONDS)) {
                    srv.shutdownNow();
                    srv.awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException ex) {
                srv.shutdownNow();
            }
        }));

        health.setStatus("", HealthCheckResponse.ServingStatus.SERVING);

        srv.awaitTermination();
    }
}
