package opp.mic.cms.config;

import opp.mic.cms.impl.AuditAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditAware(){
        return  new AuditAwareImpl();
    }
}
