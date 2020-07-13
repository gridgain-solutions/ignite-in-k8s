package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/lease")
public class Lease {
    @PostMapping("/grant")
    public Rpc.LeaseGrantResponse grant(Rpc.LeaseGrantRequest req) {
        return Rpc.LeaseGrantResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/revoke")
    public Rpc.LeaseRevokeResponse revoke(Rpc.LeaseRevokeRequest req) {
        return Rpc.LeaseRevokeResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/keepalive")
    public Rpc.LeaseKeepAliveResponse revoke(Rpc.LeaseKeepAliveRequest req) {
        return Rpc.LeaseKeepAliveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/timetolive")
    public Rpc.LeaseTimeToLiveResponse revoke(Rpc.LeaseTimeToLiveRequest req) {
        return Rpc.LeaseTimeToLiveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/leases")
    public Rpc.LeaseLeasesResponse leases(Rpc.LeaseLeasesRequest req) {
        return Rpc.LeaseLeasesResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
