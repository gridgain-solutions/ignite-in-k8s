FROM openjdk:8-jre-alpine

ENV PRODUCT ignite-etcd
ENV IGNITE_HOME /opt/${PRODUCT}

WORKDIR /opt/${PRODUCT}

# Add missing software
#  - musl: ITDS-961
RUN apk add --upgrade \
        bash \
        wget \
        musl musl-utils libbz2 libtasn1 \
    && \
    rm -rfv /var/cache/apk/*

COPY ${PRODUCT}/build/install/${PRODUCT} /opt/${PRODUCT}

RUN mkdir ${IGNITE_HOME}/config
COPY docs/ignite-server.xml /opt/${PRODUCT}/config/
COPY docs/java.util.logging.properties /opt/${PRODUCT}/config/

ENV JAVA_OPTS -Djava.util.logging.config.file=/opt/${PRODUCT}/config/java.util.logging.properties -Djava.net.preferIPv4Stack=true

CMD  /opt/${PRODUCT}/bin/${PRODUCT} --server.port=2379 --ignite.config=/opt/${PRODUCT}/config/ignite-server.xml

RUN mkdir ${IGNITE_HOME}/work && \
    chgrp -R 0 ${IGNITE_HOME}/work && \
    chmod -R g=u ${IGNITE_HOME}/work

USER 10000

EXPOSE 2379 11211 47100 47500 49112 10800
