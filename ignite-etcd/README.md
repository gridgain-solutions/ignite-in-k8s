# ignite-etcd

Self-hosted web server exposing with embedded Ignite server node
[etcd-compatible REST endpoints](https://github.com/etcd-io/etcd/blob/release-3.4/etcdserver/etcdserverpb/rpc.proto).

## Build
`gradle installDist`

## Run
`./build/install/ignite-etcd/bin/ignite-etcd --server.port=2379`