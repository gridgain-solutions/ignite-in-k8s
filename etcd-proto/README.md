# etcd-proto

Etcd Java API auto-generated from [protobuf specs](https://github.com/etcd-io/etcd/blob/release-3.4/etcdserver/etcdserverpb/rpc.proto)

## Build
A development-only work "hack" to enable Intellij IDEA indexing of the generated classes is to make a symlink from 
the `main` source set to the generated `java` sources:
```shell script
gradle build
cd src/main
ln -s ../../build/generated/source/proto/main/java java
cd java/etcdserverpb
for l in $(ls -1 ../../grpc/etcdserverpb); do ln -s ../../grpc/etcdserverpb/$l $l; done
```
Remove the `java` symlink before releasing this module: `rm src/main/java`