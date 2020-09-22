package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.AuthGrpc;
import etcdserverpb.Rpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

public final class Auth extends AuthGrpc.AuthImplBase {
    private final com.futurewei.ignite.etcd.Auth impl;

    public Auth(Ignite ignite) {
        impl = new com.futurewei.ignite.etcd.Auth(ignite);
    }

    @Override
    public void authEnable(Rpc.AuthEnableRequest req, StreamObserver<Rpc.AuthEnableResponse> res) {
        try {
            res.onNext(impl.authEnable(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void authDisable(Rpc.AuthDisableRequest req, StreamObserver<Rpc.AuthDisableResponse> res) {
        try {
            res.onNext(impl.authDisable(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void authenticate(Rpc.AuthenticateRequest req, StreamObserver<Rpc.AuthenticateResponse> res) {
        try {
            res.onNext(impl.authenticate(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userAdd(Rpc.AuthUserAddRequest req, StreamObserver<Rpc.AuthUserAddResponse> res) {
        try {
            res.onNext(impl.userAdd(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userGet(Rpc.AuthUserGetRequest req, StreamObserver<Rpc.AuthUserGetResponse> res) {
        try {
            res.onNext(impl.userGet(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userList(Rpc.AuthUserListRequest req, StreamObserver<Rpc.AuthUserListResponse> res) {
        try {
            res.onNext(impl.userList(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userDelete(Rpc.AuthUserDeleteRequest req, StreamObserver<Rpc.AuthUserDeleteResponse> res) {
        try {
            res.onNext(impl.userDelete(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userChangePassword(
            Rpc.AuthUserChangePasswordRequest req,
            StreamObserver<Rpc.AuthUserChangePasswordResponse> res
    ) {
        try {
            res.onNext(impl.userChangePassword(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userGrantRole(Rpc.AuthUserGrantRoleRequest req, StreamObserver<Rpc.AuthUserGrantRoleResponse> res) {
        try {
            res.onNext(impl.userGrantRole(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void userRevokeRole(Rpc.AuthUserRevokeRoleRequest req, StreamObserver<Rpc.AuthUserRevokeRoleResponse> res) {
        try {
            res.onNext(impl.userRevokeRole(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleAdd(Rpc.AuthRoleAddRequest req, StreamObserver<Rpc.AuthRoleAddResponse> res) {
        try {
            res.onNext(impl.roleAdd(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleGet(Rpc.AuthRoleGetRequest req, StreamObserver<Rpc.AuthRoleGetResponse> res) {
        try {
            res.onNext(impl.roleGet(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleList(Rpc.AuthRoleListRequest req, StreamObserver<Rpc.AuthRoleListResponse> res) {
        try {
            res.onNext(impl.roleList(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleDelete(Rpc.AuthRoleDeleteRequest req, StreamObserver<Rpc.AuthRoleDeleteResponse> res) {
        try {
            res.onNext(impl.roleDelete(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleGrantPermission(
            Rpc.AuthRoleGrantPermissionRequest req,
            StreamObserver<Rpc.AuthRoleGrantPermissionResponse> res
    ) {
        try {
            res.onNext(impl.roleGrantPermission(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void roleRevokePermission(
            Rpc.AuthRoleRevokePermissionRequest req,
            StreamObserver<Rpc.AuthRoleRevokePermissionResponse> res
    ) {
        try {
            res.onNext(impl.roleRevokePermission(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }
}
