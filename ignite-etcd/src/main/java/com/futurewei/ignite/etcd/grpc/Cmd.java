package com.futurewei.ignite.etcd.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.services.HealthStatusManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Cmd {
    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
        Options options = new Options()
                .addOption("p", "server.port", true, "server port (default 2379)");
        CommandLine cmd = new DefaultParser().parse(options, args);

        int srvPort = cmd.hasOption("p") ? Integer.parseInt(cmd.getOptionValue("p")) : 2397;

        HealthStatusManager health = new HealthStatusManager();
        final Server server = ServerBuilder.forPort(srvPort)
                .addService(new Auth())
                .addService(new Cluster())
                .addService(new KV())
                .addService(new Lease())
                .addService(new Maintenance())
                .addService(new Watch())
                .addService(ProtoReflectionService.newInstance())
                .addService(health.getHealthService())
                .build()
                .start();

        System.out.println("Listening on port " + srvPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            try {
                if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                    server.shutdownNow();
                    server.awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException ex) {
                server.shutdownNow();
            }
        }));

        health.setStatus("", HealthCheckResponse.ServingStatus.SERVING);

        server.awaitTermination();
    }
}
