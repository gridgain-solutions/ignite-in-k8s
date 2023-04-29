#!/bin/bash

# Recommended for ignite/gridgain.
# Set to 10, if persistance is enabled, set to 0
sudo sysctl -w vm.swappiness=10

# Start an ignite-etcd shim/client node on this server 
ignite-etcd --ignite-config $IGNITE_ETCD_CONFIG/etcd-shim-node.xml

