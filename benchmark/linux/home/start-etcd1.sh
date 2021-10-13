#!/bin/bash
# Start up etcd node 1

etcd --name etcd1 --initial-advertise-peer-urls http://${SERVER_1_IP}:2380 \
  --listen-peer-urls http://${SERVER_1_IP}:2380 \
  --listen-client-urls http://${SERVER_1_IP}:2379,http://127.0.0.1:2379 \
  --advertise-client-urls http://${SERVER_1_IP}:2379

# Note that when even you start a new etcd cluster from scratch for the first time, you should also add the following additional
# parameters to the startup command above (unless they are already provided for as environment variables in the .profile or .bashrc file).
# Ensure that these parameters are provided (only for initial starts of new clusters from scratch), either here or in your .profile or .bashrc env file.
# They should then be commented out after the cluster is fully started, so that they do not interfere with subsequent restarts (not from scratch).
# ALSO NOTE!!! if you do enable these parameters here, the --intial-cluster-token value below "etcd-cluster-1" must be unique/different each time you start the cluster from scratch.
# --initial-cluster-token etcd-cluster-1 \
# --initial-cluster etcd1=http://${SERVER_1_IP}:2380,etcd2=http://${SERVER_2_IP}:2380,etcd3=http://${SERVER_3_IP}:2380 \
# --initial-cluster-state new
