benchmark --endpoints=$IGNITE_ETCD_ENDPOINTS --conns=100 --clients=1000 put --key-size=8 --sequential-keys --total=100000 --val-size=256
