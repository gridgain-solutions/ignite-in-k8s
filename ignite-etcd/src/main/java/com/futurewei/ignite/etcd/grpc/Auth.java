package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.AuthGrpc;
import etcdserverpb.Rpc;
import io.grpc.stub.StreamObserver;

public final class Auth extends AuthGrpc.AuthImplBase {
    private final com.futurewei.ignite.etcd.Auth impl = new com.futurewei.ignite.etcd.Auth();

    @Override
    public void authEnable(Rpc.AuthEnableRequest req, StreamObserver<Rpc.AuthEnableResponse> res) {
        res.onNext(impl.authEnable(req));
        res.onCompleted();
    }

    @Override
    public void authDisable(Rpc.AuthDisableRequest req, StreamObserver<Rpc.AuthDisableResponse> res) {
        res.onNext(impl.authDisable(req));
        res.onCompleted();
    }

    @Override
    public void authenticate(Rpc.AuthenticateRequest req, StreamObserver<Rpc.AuthenticateResponse> res) {
        res.onNext(impl.authenticate(req));
        res.onCompleted();
    }

    @Override
    public void userAdd(Rpc.AuthUserAddRequest req, StreamObserver<Rpc.AuthUserAddResponse> res) {
        res.onNext(impl.userAdd(req));
        res.onCompleted();
    }

    @Override
    public void userGet(Rpc.AuthUserGetRequest req, StreamObserver<Rpc.AuthUserGetResponse> res) {
        res.onNext(impl.userGet(req));
        res.onCompleted();
    }

    @Override
    public void userList(Rpc.AuthUserListRequest req, StreamObserver<Rpc.AuthUserListResponse> res) {
        res.onNext(impl.userList(req));
        res.onCompleted();
    }

    @Override
    public void userDelete(Rpc.AuthUserDeleteRequest req, StreamObserver<Rpc.AuthUserDeleteResponse> res) {
        res.onNext(impl.userDelete(req));
        res.onCompleted();
    }

    @Override
    public void userChangePassword(
            Rpc.AuthUserChangePasswordRequest req,
            StreamObserver<Rpc.AuthUserChangePasswordResponse> res
    ) {
        res.onNext(impl.userChangePassword(req));
        res.onCompleted();
    }

    @Override
    public void userGrantRole(Rpc.AuthUserGrantRoleRequest req, StreamObserver<Rpc.AuthUserGrantRoleResponse> res) {
        res.onNext(impl.userGrantRole(req));
        res.onCompleted();
    }

    @Override
    public void userRevokeRole(Rpc.AuthUserRevokeRoleRequest req, StreamObserver<Rpc.AuthUserRevokeRoleResponse> res) {
        res.onNext(impl.userRevokeRole(req));
        res.onCompleted();
    }

    @Override
    public void roleAdd(Rpc.AuthRoleAddRequest req, StreamObserver<Rpc.AuthRoleAddResponse> res) {
        res.onNext(impl.roleAdd(req));
        res.onCompleted();
    }

    @Override
    public void roleGet(Rpc.AuthRoleGetRequest req, StreamObserver<Rpc.AuthRoleGetResponse> res) {
        res.onNext(impl.roleGet(req));
        res.onCompleted();
    }

    @Override
    public void roleList(Rpc.AuthRoleListRequest req, StreamObserver<Rpc.AuthRoleListResponse> res) {
        res.onNext(impl.roleList(req));
        res.onCompleted();
    }

    @Override
    public void roleDelete(Rpc.AuthRoleDeleteRequest req, StreamObserver<Rpc.AuthRoleDeleteResponse> res) {
        res.onNext(impl.roleDelete(req));
        res.onCompleted();
    }

    @Override
    public void roleGrantPermission(
            Rpc.AuthRoleGrantPermissionRequest req,
            StreamObserver<Rpc.AuthRoleGrantPermissionResponse> res
    ) {
        res.onNext(impl.roleGrantPermission(req));
        res.onCompleted();
    }

    @Override
    public void roleRevokePermission(
            Rpc.AuthRoleRevokePermissionRequest req,
            StreamObserver<Rpc.AuthRoleRevokePermissionResponse> res
    ) {
        res.onNext(impl.roleRevokePermission(req));
        res.onCompleted();
    }
}
