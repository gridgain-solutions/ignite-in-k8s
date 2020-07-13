# ignite-etcd

Self-hosted web server exposing with embedded Ignite server node
[etcd-compatible REST endpoints](https://github.com/etcd-io/etcd/blob/release-3.4/etcdserver/etcdserverpb/rpc.proto).

## Build
`gradle bootJar`

## Run
`java -jar build/libs/ignite-etcd-1.0-SNAPSHOT.jar --server.port=2379`