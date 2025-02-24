//package com.example.sosamoapp.Config
//
//import jakarta.persistence.EntityManagerFactory
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.jdbc.DataSourceBuilder
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.orm.jpa.JpaTransactionManager
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
//import org.springframework.transaction.PlatformTransactionManager
//import org.springframework.transaction.annotation.EnableTransactionManagement
//import javax.sql.DataSource
//
//@Configuration
//@EnableTransactionManagement
//class DatabaseConfig {
//
//    @Value("\${spring.datasource.url}")
//    private lateinit var dataSourceUrl: String
//
//    @Value("\${spring.datasource.username}")
//    private lateinit var username: String
//
//    @Value("\${spring.datasource.password}")
//    private lateinit var password: String
//
//    @Value("\${spring.datasource.driver-class-name}")
//    private lateinit var driverClassName: String
//
//    @Bean
//    fun dataSource(): DataSource {
//        return DataSourceBuilder.create()
//            .driverClassName(driverClassName)
//            .url(dataSourceUrl)
//            .username(username)
//            .password(password)
//            .build()
//    }
//
//    @Bean
//    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
//        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
//        entityManagerFactoryBean.dataSource = dataSource
//
//        // Hibernate JPA vendor adapter
//        val vendorAdapter = HibernateJpaVendorAdapter()
//        entityManagerFactoryBean.jpaVendorAdapter = vendorAdapter
//        entityManagerFactoryBean.setPackagesToScan("com.example.Entity")
//
//        // additional settings
//        val properties: HashMap<String, Any> = HashMap()
//        properties["hibernate.dialect"] = "org.hibernate.dialect.MySQL5InnoDBDialect"
//        entityManagerFactoryBean.setJpaPropertyMap(properties)
//
//        return entityManagerFactoryBean
//    }
//
//    @Bean
//    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
//        return JpaTransactionManager(entityManagerFactory)
//    }
//}