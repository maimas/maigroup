package com.mg.persistence.config;

import com.mg.persistence.exceptions.RuntimeConfigurationException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoDBConfig {
/*

    @Value("${spring.data.mongodb.host}")
    private String MONGO_DB_HOST;

    @Value("${spring.data.mongodb.port:27017}")
    private int MONGO_DB_PORT;
*/

    @Value("${spring.data.mongodb.uri}")
    private String MONGO_DB_URI;

    @Value("${spring.data.mongodb.database}")
    private String MONGO_DB_NAME;

    private static MongoClient client;


    @Bean
    public MongoClient mongoClient() {
        return getClient();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        ServerAddress server = getClient().getAddress();

        if (server != null) {
            MongoClient mongoClient = new MongoClient(server.getHost(), server.getPort());
            return new MongoTemplate(mongoClient, MONGO_DB_NAME);
        }

        throw new RuntimeConfigurationException("Could not initialize mongo template. Server address is null");
    }

    @Bean
    public GridFsTemplate gridFsTemplate() {
        return new GridFsTemplate(mongoTemplate().getMongoDbFactory(), mongoTemplate().getConverter());
    }


    public MongoDatabase getDatabase() {
        return getClient().getDatabase(MONGO_DB_NAME);
    }

    public String getDatabaseName() {
        return getDatabase().getName();
    }

    public MongoClient getClient() {
        if (client == null) {
            synchronized (MongoClient.class) {
                if (client == null) {
                    client = new MongoClient(new MongoClientURI(MONGO_DB_URI));
                }
            }
        }
        return client;
    }

}