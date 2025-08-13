package com.llmreview.api.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ApplicationTestContainers {
    @Container
    protected static final MongoDBContainer container = new MongoDBContainer("mongo:7.0.23-rc1-jammy")
        .withExposedPorts(27017);

    @DynamicPropertySource
    protected static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

}
