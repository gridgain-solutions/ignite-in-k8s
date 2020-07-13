package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;

// TODO: protobuf-agnostic
public final class Auth {
    public Rpc.AuthEnableResponse authEnable(Rpc.AuthEnableRequest req) {
        return Rpc.AuthEnableResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthDisableResponse authDisable(Rpc.AuthDisableRequest req) {
        return Rpc.AuthDisableResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthenticateResponse authenticate(Rpc.AuthenticateRequest req) {
        return Rpc.AuthenticateResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserAddResponse userAdd(Rpc.AuthUserAddRequest req) {
        return Rpc.AuthUserAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserGetResponse userGet(Rpc.AuthUserGetRequest req) {
        return Rpc.AuthUserGetResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserListResponse userList(Rpc.AuthUserListRequest req) {
        return Rpc.AuthUserListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserDeleteResponse userDelete(Rpc.AuthUserDeleteRequest req) {
        return Rpc.AuthUserDeleteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserChangePasswordResponse userChangePassword(Rpc.AuthUserChangePasswordRequest req) {
        return Rpc.AuthUserChangePasswordResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserGrantRoleResponse userGrantRole(Rpc.AuthUserGrantRoleRequest req) {
        return Rpc.AuthUserGrantRoleResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthUserRevokeRoleResponse userRevokeRole(Rpc.AuthUserRevokeRoleRequest req) {
        return Rpc.AuthUserRevokeRoleResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleAddResponse roleAdd(Rpc.AuthRoleAddRequest req) {
        return Rpc.AuthRoleAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleGetResponse roleGet(Rpc.AuthRoleGetRequest req) {
        return Rpc.AuthRoleGetResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleListResponse roleList(Rpc.AuthRoleListRequest req) {
        return Rpc.AuthRoleListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleDeleteResponse roleDelete(Rpc.AuthRoleDeleteRequest req) {
        return Rpc.AuthRoleDeleteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleGrantPermissionResponse roleGrantPermission(Rpc.AuthRoleGrantPermissionRequest req) {
        return Rpc.AuthRoleGrantPermissionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.AuthRoleRevokePermissionResponse roleRevokePermission(Rpc.AuthRoleRevokePermissionRequest req) {
        return Rpc.AuthRoleRevokePermissionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
