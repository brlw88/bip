package me.brlw.bip.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * Created by ww on 28.09.16.
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"me.brlw.bip.account", "me.brlw.bip.redirection", "me.brlw.bip.statistics"})
public class JpaConfig {

    @Bean
    public DataSource dataSource()
    {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.addScripts("classpath:META-INF/config/schema.sql", "classpath:META-INF/config/test-data.sql");
        builder.setType(EmbeddedDatabaseType.H2);
        return builder.build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        emf.setJpaDialect(jpaDialect());
        emf.setPackagesToScan("me.brlw.bip.account", "me.brlw.bip.redirection", "me.brlw.bip.statistics");
        emf.setMappingResources();

        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.max_fetch_depth", 3);
        props.put("hibernate.jdbc.fetch_size", 50);
        props.put("hibernate.jdbc.batch_size", 10);
        props.put("hibernate.show_sql", true);

        emf.setJpaProperties(props);
        emf.afterPropertiesSet();

        return emf;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return adapter;
    }

    @Bean
    public JpaDialect jpaDialect() {
        JpaDialect jpaDialect = new HibernateJpaDialect();
        return jpaDialect;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
