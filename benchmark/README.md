# benchmark
*Temporary Disclaimer:*  This branch is complete in content, but is not yet in a clonable state.  The instructions herein are not yet fully accurate, and perhaps should be converted into a Docker container version.

The intended result is 5 servers: 3 server nodes, 1 client/shim node, and 1 benchmark node.

Each of the nodes should contain in $HOME:  
(1) At most two startup (or benchmark) scripts copied from the repo/branch folder 'home' to the Unbuntu $HOME folder.  
    The startup/benchmark scripts are:  
    For server node 1: start-etcd1.sh and start-ignite.sh  
    For server node 2: start-etcd2.sh and start-ignite.sh  
    For server node 3: start-etcd3.sh and start-ignite.sh  
    For the client/shim node: start-ignite-etcd-shim.sh
    For the benchmark node: etcd-bm-write-all.sh and ignite-etcd-bm-write-all.sh
    
(2) A 'dev' folder that should contain the following sub-folders:  
    etcd  go  igk8s  igk8s-config  ignite 
    
(3) The benchmark node (just for consistency) can have the same setup above, but much of it will not be used.  Instead, it will have one addition, the benchmark Go app. The benchmark installation instructions herein will install the benchmark app into GoPath and the app itself into $HOME/dev/go/bin.  It is runnable from any locaton on the server.

Please contact me for help/questions until I can get this branch in a fully clonable state.

*End Temporary Disclaimer.*

This branch contains the additional configuration files and instructions necessary to run *published etcd benchmarks*
on Ubuntu vms, for either standard etcd or custom ignite-etcd configurations.

The folders/files herein are scripted for Unbuntu 20.0.4 and are organized into the folders assumed by the current configuration scripts.
However, these configuration files can be easily changed for alternative Linux distros and/or other desired build configurations.

Once fully configured, the etcd benchmarks can be run for only one configuration at a time (either for etcd or for ignite-ectd).

The benchmark folders are:

- [home folder](benchmark/dev-guide.md): startup and benchmark scripts to be added to Ubuntu $HOME directory (for the etcd and ignite-etcd configurations).
- [env folder](benchmark/design.md): contains only one file named .profile-additions.  Add all the additions in this file to the end of your .profile or .bashrc file.
- [dev folder](benchmark/design.md): top level folder under $HOME. All other benchmark folders, files, binaries, etc. will be added under this 'dev' folder.
- [ig-ik8s-config](ignite-etcd/README.md): contains ignite-etcd specific configuration files.

Below are the detailed instructions for installing and building the benchmark applications and etcd/ignite-etcd servers.
**Please note** that these instructions incrementally add all the .profile extensions listed above in .profile-additions file.
So you can copy all the .profile extensions at once, or build them step by step, and compare them at the end.

To install Java 8 on Ubunutu server/machine:
-------------------------------------------
sudo apt update  
sudo apt install openjdk-8-jdk openjdk-8-jre  
java -version

add to ~/.profile:  
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64  
export JVM_OPTS="-Dfile.encoding=UTF-8 -server -Xms4g -Xmx4g -XX:+UseG1GC -XX:+DisableExplicitGC -Djava.net.preferIPv4Stack=true -XX:MaxMetaspaceSize=1g"  

$ source .profile


To install Go on Ubuntu server/machine:
--------------------------------------
*Be sure to edit the Go version you want, e.g. 16.8 below*

wget -c https://dl.google.com/go/go1.16.8.linux-amd64.tar.gz -O - | sudo tar -xz -C /usr/local

add to ~/.profile:  
export PATH=$PATH:/usr/local/go/bin

optionally add to ~/.profile a path for Go projects such as:  
export GOPATH=$HOME/dev/go  
export PATH=$PATH:$GOPATH/bin

$ source .profile  
$ go version  

To install etcd
----------------
(1) Open in a browser the desired release link, e.g. for 3.5: https://github.com/etcd-io/etcd/releases/tag/v3.5.0  
(2) Copy the install script (displayed on the etcd webpage) directly into your ubuntu terminal session (maybe they will change that someday :)  
(3) The copied script will download etcd into folder: /tmp/etcd-download-test  
(4) copy the three etcd binaries (etcd, etcdctl, etcdutl) to /usr/local/bin:  
    sudo cp /tmp/etcd-download-test/etcd /usr/local/bin/  
    sudo cp /tmp/etcd-download-test/etcdctl /usr/local/bin/  
    sudo cp /tmp/etcd-download-test/etcdutl /usr/local/bin/  
(5) verify with:  etcd -version  
      etcd Version: 3.5.0  
      Git SHA: 946a5a6f2  
      Go Version: go1.16.3  
      Go OS/Arch: linux/amd64  
(6) Move or delete the /tmp/ectcd-download-test folder (and its contents) as desired (or just leave it as is).  No other etcd files are not needed for this project.  

VERY IMPORTANT: FOR THE FIRST TIME YOU START UP A MULTI_NODE ETCD CLUSTER
-------------------------------------------------------------------------
Starting etcd clusters is generally very easy (per on-line documentation).  But restarting, resetting, clearing etcd clusters, once started can be very confusing until some basics are understood.  
(1) Every time you start a "NEW" cluster (not restart an already existing cluster), you must provide a new unqiue token ID (see the .profile example below).  
(2) If you want to start over with a new cluster, you should delete the etcd db directory (called default.etcd unless you rename it) on all servers, AND change the new cluster token ID (in the .profile example below) -- this is very important or very strange things start to happen.  
(3) There are probably better ways to do this, but they require a good deal more understanding of etcd (refer to the documentation if you need this understanding).  
(4) Basically, understand and set these environment variables (any time you start a new cluster), and uncomment them after the cluster is fully up (yes, wierd).  
#export ETCD_INITIAL_CLUSTER="etcd1=http://${SERVER_1_IP}:2380,etcd2=http://${SERVER_2_IP}:2380,etcd3=http://${SERVER_3_IP}:2380"  
#export ETCD_INITIAL_CLUSTER_STATE=new  
#export ETCD_INITIAL_CLUSTER_TOKEN=??? # <-- change/ensure that this value is unique/different each time a new cluster is started.  

Then to start an etcd cluster using the command below on each etcd server (in a script or terminal session):  
(The --name parameter is optional.  See the full .profile example below to see how the INITIIAL CLUSTER and SERVER_IP variables are set and automatically added)  
etcd --name etcd1 --initial-advertise-peer-urls http://${SERVER_1_IP}:2380 \  
  --listen-peer-urls http://${SERVER_1_IP}:2380 \  
  --listen-client-urls http://${SERVER_1_IP}:2379,http://127.0.0.1:2379 \  
  --advertise-client-urls http://${SERVER_1_IP}:2379

To install benchmark: (note the /v3/ below -- make sure the benchmark is for your desired version)
--------------------------------------------------------------------------------------------------

$ go get go.etcd.io/etcd/v3/tools/benchmark  
$ benchmark --help

For the ignite-etc installs, create a home directory, such as dev:
------------------------------------------------------------------
cd $HOME  
mkdir dev


To install ignite v2.10.0 (binaries only)
-----------------------------------------
cd $HOME/dev  
wget https://archive.apache.org/dist/ignite/2.10.0/apache-ignite-2.10.0-bin.zip

If necessary (install unzip)  
sudo apt-get install unzip  
unzip apache-ignite-2.10.0-bin.zip  

mv apache-ignite-2.10.0-bin ignite  (simplify name to ignite)  
rm apache-ignite-2.10.0-bin.zip (delete the .zip file if you want to).  

To install and build ignite-in-k8s
-----------------------------------
(1) Make sure you are in the 'dev' directory (or the installation folder of choice)  
(2) $ git clone https://github.com/gridgain-solutions/ignite-in-k8s.git  
(3) optionally rename folder to shorter name like: igk8s  
    This project generates a really long path, that can exceed limits on Windows, but is probably not a problem on Linux.  
(4) cd igk8s/ignite-etcd  
(5) ./gradlew installDist (to build the project)  


Setting up ETCD Endpoints (to use in etcdcl and benchmark commands)
-------------------------------------------------------------------
All etcdctl and benchmark commands are issused to the same listening port (2379), so set up some env variables for the different etcd configs you want to test.
For etcd -- this will be a list of (n) etcd servers.  For ignite-etcd, it may be a single etcd-shim server.  See examples below:

# Set etcd endpoints (for benchmark and client apps)
export ETCD_ENDPOINTS=${SERVER_1_IP}:2379,${SERVER_2_IP}:2379,${SERVER_3_IP}:2379  
export IGNITE_ETCD_ENDPOINTS=${SERVER_4_IP}:2379

Then in etcdctl commands, add the --endpoints=$ETCD_ENDPOINTS  (or IGNITE_ETCD_ENDPOINTS) to the etcdctl command you want to invoke.  (note the = sign).  
For the benchmark program, omit the '=', so add --endpoints $ETCD_ENDPOINTS  to the benchmark command.

With the two environments variables, you should be able to run all etcdctl or benchmark commands for either etcd or ignite-etcd.  But of course you can only have one test configuration active at a time (if you are using the same machines).  As both etcd and ignite_etcd have to listen on the same port 2379.

# Example .profile 
Below is an example of the env variables and settings added to the ubuntu .profile file (for the tests ran in GCE):

\# Set Java Home and ignite optimized JVM settings  
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64  
export JVM_OPTS="-Dfile.encoding=UTF-8 -server -Xms4g -Xmx4g -XX:+UseG1GC -XX:+DisableExplicitGC -Djava.net.preferIPv4Stack=true -XX:MaxMetaspaceSize=1g"  

\# Set GO environment variables and update PATH  
export GOPATH=$HOME/dev/go  
export PATH=$PATH:$GOPATH/bin  
export PATH=$PATH:/usr/local/go/bin  

\# Set ignite HOME and update PATH  
export IGNITE_HOME=${HOME}/dev/ignite  
export PATH=$PATH:${IGNITE_HOME}/bin  

\# Set ignite-etcd HOME and update PATH  
export IGNITE_ETCD_CONFIG=${HOME}/dev/igk8s-configs  
export IGNITE_ETCD_HOME=${HOME}/dev/igk8s/ignite-etcd/build/install/ignite-etcd  
export PATH=$PATH:${IGNITE_ETCD_HOME}/bin  

\# Set Internal (Static) Server IPs  
export SERVER_1_IP=10.162.0.2  
export SERVER_2_IP=10.162.0.3  
export SERVER_3_IP=10.162.0.11  
export SERVER_4_IP=10.162.0.9  
export SERVER_5_IP=10.162.0.10  

\# Set etcd configuration flags (common across all etcd nodes) using reserved etcd environment variables  
export ETCDCTL_API=3  
export ETCD_DATA_DIR=${HOME}/dev/etcd/data.etcd

\# Set these only when starting up a new etcd cluster for the first time!  
\# After the cluster is up and running, comment out these settings (and run source .profile)  
\#export ETCD_INITIAL_CLUSTER="etcd1=http://${SERVER_1_IP}:2380,etcd2=http://${SERVER_2_IP}:2380,etcd3=http://${SERVER_3_IP}:2380"  
\#export ETCD_INITIAL_CLUSTER_STATE=new  
\#export ETCD_INITIAL_CLUSTER_TOKEN=??? # <-- change/ensure that this value is unique/different each time a new cluster is started.  

\# Set etcd endpoints (for benchmark and client apps)  
export ETCD_ENDPOINTS=${SERVER_1_IP}:2379,${SERVER_2_IP}:2379,${SERVER_3_IP}:2379  
export IGNITE_ETCD_ENDPOINTS=${SERVER_4_IP}:2379
