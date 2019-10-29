package com.mg.persistence.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private static MongoClient client;


    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    public MongoDatabase getDatabase() {
        return getClient().getDatabase(databaseName);
    }

    @Override
    public String getDatabaseName() {
        return getDatabase().getName();
    }

    public MongoClient getClient() {
        if (client == null) {
            synchronized (MongoClient.class) {
                if (client == null) {
                    client = new MongoClient(new MongoClientURI(connectionString));
                }
            }
        }
        return client;
    }

    @Override
    public MongoClient mongoClient() {
        return getClient();
    }
}