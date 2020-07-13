package com.futurewei.ignite.etcd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

@SpringBootApplication
public class SpringEtcd {
    public static void main(String[] args) {
        SpringApplication.run(SpringEtcd.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    Cache<Key, Value> store() {
        return Caching.getCachingProvider().getCacheManager().createCache("etcd", new MutableConfiguration<>());
    }
}
