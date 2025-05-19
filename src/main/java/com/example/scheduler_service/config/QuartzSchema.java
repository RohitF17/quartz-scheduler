package com.example.scheduler_service.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class QuartzSchema implements ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    @Override
       public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("quartz-schema.sql"));
            System.out.println("✅ Quartz schema initialized successfully.");
        } catch (Exception e) {
            System.out.println("⚠️ Quartz schema might already exist or failed to initialize: " + e.getMessage());
        }
    }

}

    
