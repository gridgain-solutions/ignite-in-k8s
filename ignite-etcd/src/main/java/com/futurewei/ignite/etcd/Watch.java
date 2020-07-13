package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Watch {
    @PostMapping("/v3/watch")
    public Rpc.WatchResponse watch(Rpc.WatchRequest req) {
        return Rpc.WatchResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
