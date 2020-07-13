package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/auth")
public class Auth {
    @PostMapping("/enable")
    public Rpc.AuthEnableResponse enable(Rpc.AuthEnableRequest req) {
        return Rpc.AuthEnableResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/disable")
    public Rpc.AuthDisableResponse disable(Rpc.AuthDisableRequest req) {
        return Rpc.AuthDisableResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/authenticate")
    public Rpc.AuthenticateResponse authenticate(Rpc.AuthenticateRequest req) {
        return Rpc.AuthenticateResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/add")
    public Rpc.AuthUserAddResponse userAdd(Rpc.AuthUserAddRequest req) {
        return Rpc.AuthUserAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/get")
    public Rpc.AuthUserGetResponse userGet(Rpc.AuthUserGetRequest req) {
        return Rpc.AuthUserGetResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/list")
    public Rpc.AuthUserListResponse userList(Rpc.AuthUserListRequest req) {
        return Rpc.AuthUserListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/delete")
    public Rpc.AuthUserDeleteResponse userDelete(Rpc.AuthUserDeleteRequest req) {
        return Rpc.AuthUserDeleteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/changepw")
    public Rpc.AuthUserChangePasswordResponse userChangePassword(Rpc.AuthUserChangePasswordRequest req) {
        return Rpc.AuthUserChangePasswordResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/grant")
    public Rpc.AuthUserGrantRoleResponse userGrantRole(Rpc.AuthUserGrantRoleRequest req) {
        return Rpc.AuthUserGrantRoleResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/user/revoke")
    public Rpc.AuthUserRevokeRoleResponse userRevokeRole(Rpc.AuthUserRevokeRoleRequest req) {
        return Rpc.AuthUserRevokeRoleResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/add")
    public Rpc.AuthRoleAddResponse roleAdd(Rpc.AuthRoleAddRequest req) {
        return Rpc.AuthRoleAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/get")
    public Rpc.AuthRoleGetResponse roleGet(Rpc.AuthRoleGetRequest req) {
        return Rpc.AuthRoleGetResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/list")
    public Rpc.AuthRoleListResponse roleList(Rpc.AuthRoleListRequest req) {
        return Rpc.AuthRoleListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/delete")
    public Rpc.AuthRoleDeleteResponse roleDelete(Rpc.AuthRoleDeleteRequest req) {
        return Rpc.AuthRoleDeleteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/grant")
    public Rpc.AuthRoleGrantPermissionResponse roleGrantPermission(Rpc.AuthRoleGrantPermissionRequest req) {
        return Rpc.AuthRoleGrantPermissionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/role/revoke")
    public Rpc.AuthRoleRevokePermissionResponse roleRevokePermission(Rpc.AuthRoleRevokePermissionRequest req) {
        return Rpc.AuthRoleRevokePermissionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
