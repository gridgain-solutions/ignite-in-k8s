package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/maintenance")
public class Maintenance {
    @PostMapping("/alarm")
    public Rpc.AlarmResponse alarm(Rpc.AlarmRequest req) {
        return Rpc.AlarmResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/status")
    public Rpc.StatusResponse status(Rpc.StatusRequest req) {
        return Rpc.StatusResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/defragment")
    public Rpc.DefragmentResponse defragment(Rpc.DefragmentRequest req) {
        return Rpc.DefragmentResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/hash")
    public Rpc.HashResponse hash(Rpc.HashRequest req) {
        return Rpc.HashResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/snapshot")
    public Rpc.SnapshotResponse snapshot(Rpc.SnapshotRequest req) {
        return Rpc.SnapshotResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/transfer-leadership")
    public Rpc.MoveLeaderResponse moveLeader(Rpc.MoveLeaderRequest req) {
        return Rpc.MoveLeaderResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
