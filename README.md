# ignite-in-k8s
Ignite as etcd replacement in Kubernetes

- [Developerment Guide](docs/dev-guide.md): how to build and run Kubernetes with Ignite on a local machine
- [Design Document](docs/design.md): General overview of the Shim layer design
- [etcd-proto](etcd-proto/README.md): auto-generated etcd Java API
- [ignite-etcd](ignite-etcd/README.md): self-hosted gRPC server with embedded Ignite server node exposing 
  etcd-compatible REST endpoints.  
- [Phase 0 integration tests](docs/integration-test-report.md): Report and analysis of integration tests 
