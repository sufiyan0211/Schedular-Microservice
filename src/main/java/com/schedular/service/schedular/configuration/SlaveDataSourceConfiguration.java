package com.schedular.service.schedular.configuration;

import com.schedular.service.schedular.model.Url;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.schedular.service.schedular.repository.slave",
        entityManagerFactoryRef = "slaveEntityManagerFactory",
        transactionManagerRef = "slaveTransactionManager"
)
public class SlaveDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSourceProperties slaveDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave.configuration")
    public DataSource slaveDataSource() {
        return slaveDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "slaveEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory(
            EntityManagerFactoryBuilder builder
    ) {
        return builder
                .dataSource(slaveDataSource())
                .packages(Url.class)
                .build();
    }

    @Bean(name = "slaveTransactionManager")
    public PlatformTransactionManager slaveTransactionManager(
            final @Qualifier("slaveEntityManagerFactory") LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory) {
        return new JpaTransactionManager(slaveEntityManagerFactory.getObject());
    }
}
