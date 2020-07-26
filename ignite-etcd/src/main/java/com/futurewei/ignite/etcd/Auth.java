package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;

public final class Auth {
    private final EtcdCluster ctx;

    public Auth(Ignite ignite) {
        ctx = new EtcdCluster(ignite);
    }

    public Rpc.AuthEnableResponse authEnable(Rpc.AuthEnableRequest req) {
        return Rpc.AuthEnableResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthDisableResponse authDisable(Rpc.AuthDisableRequest req) {
        return Rpc.AuthDisableResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthenticateResponse authenticate(Rpc.AuthenticateRequest req) {
        return Rpc.AuthenticateResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserAddResponse userAdd(Rpc.AuthUserAddRequest req) {
        return Rpc.AuthUserAddResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserGetResponse userGet(Rpc.AuthUserGetRequest req) {
        return Rpc.AuthUserGetResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserListResponse userList(Rpc.AuthUserListRequest req) {
        return Rpc.AuthUserListResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserDeleteResponse userDelete(Rpc.AuthUserDeleteRequest req) {
        return Rpc.AuthUserDeleteResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserChangePasswordResponse userChangePassword(Rpc.AuthUserChangePasswordRequest req) {
        return Rpc.AuthUserChangePasswordResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserGrantRoleResponse userGrantRole(Rpc.AuthUserGrantRoleRequest req) {
        return Rpc.AuthUserGrantRoleResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthUserRevokeRoleResponse userRevokeRole(Rpc.AuthUserRevokeRoleRequest req) {
        return Rpc.AuthUserRevokeRoleResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleAddResponse roleAdd(Rpc.AuthRoleAddRequest req) {
        return Rpc.AuthRoleAddResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleGetResponse roleGet(Rpc.AuthRoleGetRequest req) {
        return Rpc.AuthRoleGetResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleListResponse roleList(Rpc.AuthRoleListRequest req) {
        return Rpc.AuthRoleListResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleDeleteResponse roleDelete(Rpc.AuthRoleDeleteRequest req) {
        return Rpc.AuthRoleDeleteResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleGrantPermissionResponse roleGrantPermission(Rpc.AuthRoleGrantPermissionRequest req) {
        return Rpc.AuthRoleGrantPermissionResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.AuthRoleRevokePermissionResponse roleRevokePermission(Rpc.AuthRoleRevokePermissionRequest req) {
        return Rpc.AuthRoleRevokePermissionResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }
}
