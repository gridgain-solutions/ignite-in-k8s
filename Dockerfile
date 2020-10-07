FROM openjdk:8-jre-alpine

WORKDIR /opt/ignite-etcd

# Add missing software
#  - musl: ITDS-961
RUN apk add --upgrade \
        bash \
        wget \
        musl musl-utils libbz2 libtasn1 \
    && \
    rm -rfv /var/cache/apk/*

COPY ignite-etcd/build/install/ignite-etcd /opt/ignite-etcd

RUN mkdir /opt/ignite-etcd/config
COPY docs/ignite-server-dev-local.xml /opt/ignite-etcd/config/
COPY docs/java.util.logging.properties /opt/ignite-etcd/config/

EXPOSE 2379 11211 47100 47500 49112 10800

ENV IGNITE_ETCD_OPTS -Djava.util.logging.config.file=/opt/ignite-etcd/config/java.util.logging.properties -Djava.net.preferIPv4Stack=true

CMD ["/opt/ignite-etcd/bin/ignite-etcd", "--server-port", "2379", "--ignite-config", "/opt/ignite-etcd/config/ignite-server-dev-local.xml"]