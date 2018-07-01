package org.dreamcafe.sljc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class LdapConfig {
    @Bean
    public LdapProperties ldapProperties() {
        return new LdapProperties();
    }

    @Bean
    public LdapContextSource contextSource(LdapProperties ldapProperties) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapProperties.getUrl());
        contextSource.setUserDn(ldapProperties.getPrincipal());
        contextSource.setPassword(ldapProperties.getPassword());
        contextSource.setBase(ldapProperties.getBase());
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        return ldapTemplate;
    }
}
