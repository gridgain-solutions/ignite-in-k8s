package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/cluster/member")
public class Cluster {
    @PostMapping("/add")
    public Rpc.MemberAddResponse memberAdd(Rpc.MemberAddRequest req) {
        return Rpc.MemberAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/remove")
    public Rpc.MemberRemoveResponse memberRemove(Rpc.MemberRemoveRequest req) {
        return Rpc.MemberRemoveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/update")
    public Rpc.MemberUpdateResponse memberUpdate(Rpc.MemberUpdateRequest req) {
        return Rpc.MemberUpdateResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/list")
    public Rpc.MemberListResponse memberList(Rpc.MemberListRequest req) {
        return Rpc.MemberListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/promote")
    public Rpc.MemberPromoteResponse memberPromote(Rpc.MemberPromoteRequest req) {
        return Rpc.MemberPromoteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
