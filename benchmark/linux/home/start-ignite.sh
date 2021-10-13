#!/bin/bash

# Start an standard ignite server (not an ignite-etcd server) using the config file: $IGNITE_ETCD_CONFIG/ignite-server-node.xml

# Recommended for ignite/gridgain.
# Set to 10, if persistance is enabled, set to 0
sudo sysctl -w vm.swappiness=10

# Start a standard ignite server on this server/node 
ignite.sh $IGNITE_ETCD_CONFIG/ignite-server-node.xml

