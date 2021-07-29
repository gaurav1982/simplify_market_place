package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Category.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Category.class.getName() + ".categories");
            createCache(cm, com.mycompany.myapp.domain.Category.class.getName() + ".jobPreferences");
            createCache(cm, com.mycompany.myapp.domain.Category.class.getName() + ".fields");
            createCache(cm, com.mycompany.myapp.domain.Field.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Field.class.getName() + ".categories");
            createCache(cm, com.mycompany.myapp.domain.Client.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Client.class.getName() + ".locations");
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName() + ".resumes");
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName() + ".jobPreferences");
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName() + ".locationPrefrences");
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName() + ".educations");
            createCache(cm, com.mycompany.myapp.domain.Worker.class.getName() + ".employments");
            createCache(cm, com.mycompany.myapp.domain.Resume.class.getName());
            createCache(cm, com.mycompany.myapp.domain.JobPreference.class.getName());
            createCache(cm, com.mycompany.myapp.domain.JobPreference.class.getName() + ".jobSpecificFields");
            createCache(cm, com.mycompany.myapp.domain.JobSpecificField.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Location.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Location.class.getName() + ".ids");
            createCache(cm, com.mycompany.myapp.domain.LocationPrefrence.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Education.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Employment.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Employment.class.getName() + ".ids");
            createCache(cm, com.mycompany.myapp.domain.OTP.class.getName());
            createCache(cm, com.mycompany.myapp.domain.OTPAttempt.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
