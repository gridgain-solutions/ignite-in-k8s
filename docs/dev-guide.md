# Development Guide

## Building and Running Kubernetes

- [Go development environment](https://golang.org/doc/install)
  - Export `GOPATH` environment variable explicitly.
  
- Kubernetes source code
  - `git clone --recurse-submodules https://github.com/kubernetes/kubernetes`
  - Make `K8S_REPO` environment variable point to the kubernetes repo. For example: 
    ```export K8S_REPO=`pwd`/kubernetes```
  - `cd $K8S_REPO; git checkout v1.18.5`
  
- Build Kubernetes: `cd $K8S_REPO; make -j 4`

- Build `ignite-etcd`: `cd ignite-etcd; ./gradlew installDist`

- Run Everything Locally 
  - Control Plane
    - Work directories: 
      - ```sudo mkdir -p /var/run/kubernetes; sudo chown `whoami`:`whoami` /var/run/kubernetes```
    - Etcd: 
      - ignite-etcd: `ignite-etcd/build/install/ignite-etcd/bin/ignite-etcd -Djava.net.preferIPv4Stack=true --server-port=2379 --ignite-config=docs/ignite-server.xml`
      - OR native etcd: `$K8S_REPO/third_party/etcd/etcd --listen-client-urls=http://127.0.0.1:2379 --advertise-client-urls=http://127.0.0.1:2379 --initial-cluster=http://127.0.0.1:2380 --data-dir=/tmp/default.etcd` 
    - API Server: `$K8S_REPO/_output/bin/kube-apiserver --etcd-servers=http://127.0.0.1:2379 --service-cluster-ip-range=127.0.0.1/24 --storage-media-type=application/json`
    - Scheduler: `$K8S_REPO/_output/bin/kube-scheduler --master=http://127.0.0.1:8080`
    - Controller Manager: `$K8S_REPO/_output/bin/kube-controller-manager --master=http://127.0.0.1:8080 --service-account-private-key-file=/var/run/kubernetes/apiserver.key`
    - Configuration (`alias kubectl="$K8S_REPO/_output/bin/kubectl"`):
      ```shell script
      kubectl config --kubeconfig=local set-cluster local --server=http://127.0.0.1:8080; \
      kubectl config --kubeconfig=local set-credentials ignite --username=ignite --password=ignite; \
      kubectl config --kubeconfig=local set-context ignite-local --cluster=local --namespace=default --user=ignite; \
      kubectl config --kubeconfig=local use-context ignite-local; \
      kubectl config --kubeconfig=local view > $HOME/.kube/local-config
      ``` 
  - Node 1
    - Work directories: 
      - ```sudo mkdir -p /var/lib/kubelet; sudo chown `whoami`:`whoami` /var/lib/kubelet```
      - ```sudo mkdir -p /var/lib/dockershim; sudo chown `whoami`:`whoami` /var/lib/dockershim```
    - Docker: `systemctl --user start docker`
    - Kubelet: `sudo $K8S_REPO/_output/bin/kubelet --docker-endpoint=$DOCKER_HOST --kubeconfig=$HOME/.kube/local-config --fail-swap-on=false --cgroup-driver=none`
    - Proxy: TBD

- Test
  - With `etcdctl` (`alias etcdctl="$K8S_REPO/third_party/etcd/etcdctl --endpoints=127.0.0.1:2379 -w=json --command-timeout=600s --keepalive-timeout=600s"`):
    - [Examples from etcd documentation](https://etcd.io/docs/v3.4.0/dev-guide/interacting_v3/)
    - All keys wih history: `etcdctl get "" --from-key` 
  - With `curl`:
    - `curl -s -H 'Accept: application/json' -H 'Content-Type: application/json'  -d '{"key": [0], "range_end": [0], "max_mod_revision": 7}' -X POST http://localhost:2479/v3/kv/range`
  - With `kubectl` (`alias kubectl="$K8S_REPO/_output/bin/kubectl"`):
    - `kubectl get nodes`
    - `kubectl apply -f busybox-sleep.yaml`    
    - `kubectl get pods`
    - `kubectl logs busybox-sleep`
  - With Kubernetes integration tests:
    - Comment out line `kube::etcd::start` in `$K8S_REPO/hack/make-rules/test-integration.sh`
    - Run `ignite-etcd` 
    - `cd $K8S_REPO` 
    - Example of running all the `pods` integration tests:  
      `make test-integration WHAT=./test/integration/pods GOFLAGS="-v"`
    - Example of running single test `TestPodUpdateActiveDeadlineSeconds`:  
      `make test-integration WHAT=./test/integration/pods GOFLAGS="-v" KUBE_TEST_ARGS="-run ^TestPodUpdateActiveDeadlineSeconds$"`

- Problems and Solutions
  - **Problem**: Kubernetes integration tests fail with `context deadline exceeded` error in the log, for example:  
    `error in bringing up the master: problem initializing API group "autoscaling" : context deadline exceeded`
    **Solution**: If there are `socket: too many open files` errors preceding the `context deadline exceeded` error 
    in the log then check max number of files that a process can open. Increase it if it is less than 10K.              
  - **Problem**: `ignite-etcd` failing with `OutOfMemoryError`
    **Solution**: Increase ignite-etcd JVM heap size (`export IGNITE_ETCD_OPTS=-Xmx16g` before running `ignite-etcd`)