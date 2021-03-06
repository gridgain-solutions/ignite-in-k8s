FROM openjdk:11-jre-slim

WORKDIR /opt/ignite-etcd

COPY ignite-etcd/build/install/ignite-etcd /opt/ignite-etcd

RUN mkdir /opt/ignite-etcd/config
COPY docs/ignite-server-dev-local.xml /opt/ignite-etcd/config/
COPY docs/java.util.logging.properties /opt/ignite-etcd/config/

EXPOSE 2379 11211 47100 47500 49112 10800

ENV IGNITE_ETCD_OPTS -Djava.util.logging.config.file=/opt/ignite-etcd/config/java.util.logging.properties \
                     -Djava.net.preferIPv4Stack=true \
                     -server \
                     -Xms2g \
                     -XX:+AlwaysPreTouch \
                     -XX:+UseG1GC \
                     -XX:+ScavengeBeforeFullGC \
                     -XX:+DisableExplicitGC

CMD ["/opt/ignite-etcd/bin/ignite-etcd", "--server-port", "2379", "--ignite-config", "/opt/ignite-etcd/config/ignite-server-dev-local.xml"]