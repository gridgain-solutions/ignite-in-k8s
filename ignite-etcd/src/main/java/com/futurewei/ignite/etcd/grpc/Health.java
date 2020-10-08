package com.futurewei.ignite.etcd.grpc;

import etcd.HealthGrpc;
import etcd.HealthOuterClass;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.services.HealthStatusManager;
import io.grpc.stub.StreamObserver;

/**
 * Legacy ETCD HTTP 1.1 Health endpoint. Although kube-apiserver v1.19+ switched to using native gRPC HTTP2 Health
 * service, some Kubernetes infrastructure components still need the legacy "/health" endpoint.
 */
public final class Health extends HealthGrpc.HealthImplBase {
    private final io.grpc.health.v1.HealthGrpc.HealthImplBase impl;

    public Health(io.grpc.health.v1.HealthGrpc.HealthImplBase impl) {
        this.impl = impl;
    }

    @Override
    public void check(HealthOuterClass.HealthCheckRequest req, StreamObserver<HealthOuterClass.HealthCheckResponse> res) {
        try {
            impl.check(
                io.grpc.health.v1.HealthCheckRequest.newBuilder()
                    .setService(HealthStatusManager.SERVICE_NAME_ALL_SERVICES)
                    .build(),

                new StreamObserver<HealthCheckResponse>() {
                    @Override
                    public void onNext(HealthCheckResponse implRes) {
                        res.onNext(
                            HealthOuterClass.HealthCheckResponse.newBuilder()
                                .setHealth(implRes.getStatus() == HealthCheckResponse.ServingStatus.SERVING)
                                .build()
                        );
                    }

                    @Override
                    public void onError(Throwable t) {
                        res.onNext(
                            HealthOuterClass.HealthCheckResponse.newBuilder()
                                .setHealth(false)
                                .setReason(t.getCause().getMessage())
                                .build()
                        );
                    }

                    @Override
                    public void onCompleted() {
                        res.onCompleted();
                    }
                }
            );
        } catch (Throwable t) {
            res.onNext(
                HealthOuterClass.HealthCheckResponse.newBuilder()
                    .setHealth(false)
                    .setReason(t.getCause().getMessage()).build()
            );

            res.onCompleted();
        }
    }
}
