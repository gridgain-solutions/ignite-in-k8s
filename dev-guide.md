# Development Guide

## Building and Running Kubernetes

- [Go development environment](https://golang.org/doc/install)
  - Export `GOPATH` environment variable explicitly.
  
- Kubernetes source code
  - `git clone https://github.com/kubernetes/kubernetes`
  - Make `K8S_REPO` environment variable point to the kubernetes repo. For example: 
    ```export K8S_REPO=`pwd`/kubernetes```
  - `cd $K8S_REPO; git checkout v1.18.5`
  - ```ln -s `dirname $K8S_REPO` $GOPATH/src/k8s.io```
  
- Build Kubernetes: `cd $K8S_REPO; make -j 4`

- Build `ignite-etcd`: `cd ignite-etcd; gradle installDist`

- Run Everything Locally 
  - Control Plane
    - Work directories: 
      - ```sudo mkdir -p /var/run/kubernetes; sudo chown `whoami`:`whoami` /var/run/kubernetes```
    - Etcd: 
      - ignite-etcd: `.ignite-etcd/build/install/ignite-etcd/bin/ignite-etcd --server.port=2379`
      - OR native etcd: `$K8S_REPO/third_party/etcd/etcd --listen-client-urls=http://127.0.0.1:2379 --advertise-client-urls=http://127.0.0.1:2379 --data-dir=/tmp/default.etcd` 
    - API Server: `$K8S_REPO/_output/bin/kube-apiserver --etcd-servers=http://127.0.0.1:2379 --service-cluster-ip-range=127.0.0.1/24`
    - Scheduler: `$K8S_REPO/_output/bin/kube-scheduler --master=http://127.0.0.1:8080`
    - Controller Manager: `$K8S_REPO/_output/bin/kube-controller-manager --master=http://127.0.0.1:8080 --service-account-private-key-file=/var/run/kubernetes/apiserver.key`
    - Configuration: 
      - `$K8S_REPO/_output/bin/kubectl config --kubeconfig=local set-cluster local --server=http://127.0.0.1:8080`
      - `$K8S_REPO/_output/bin/kubectl config --kubeconfig=local set-credentials ignite --username=ignite --password=ignite`
      - `$K8S_REPO/_output/bin/kubectl config --kubeconfig=local set-context ignite-local --cluster=local --namespace=default --user=ignite`
      - `$K8S_REPO/_output/bin/kubectl config --kubeconfig=local use-context ignite-local`
      - `$K8S_REPO/_output/bin/kubectl config --kubeconfig=local view > $HOME/.kube/local-config`
  - Node 1
    - Work directories: 
      - ```sudo mkdir -p /var/lib/kubelet; sudo chown `whoami`:`whoami` /var/lib/kubelet```
      - ```sudo mkdir -p /var/lib/dockershim; sudo chown `whoami`:`whoami` /var/lib/dockershim```
    - Docker: `systemctl --user start docker`
    - Kubelet: `sudo $K8S_REPO/_output/bin/kubelet --docker-endpoint=$DOCKER_HOST --kubeconfig=$HOME/.kube/local-config --fail-swap-on=false --cgroup-driver=none`
    - Proxy: TBD

- Test
  - `$K8S_REPO/_output/bin/kubectl get nodes`
  - `$K8S_REPO/_output/bin/kubectl apply -f busybox-sleep.yaml`    
  - `$K8S_REPO/_output/bin/kubectl get pods`
  - `$K8S_REPO/_output/bin/kubectl logs busybox-sleep`