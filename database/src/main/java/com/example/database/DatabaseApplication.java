package com.example.database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class DatabaseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.exit(0);
    }
}
