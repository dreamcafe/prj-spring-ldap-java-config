package org.dreamcafe.sljc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.SearchScope;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"org.dreamcafe.sljc.server, org.dreamcafe.sljc.repo"})
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
        contextSource.setBase(ldapProperties.getBaseDn());
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setDefaultSearchScope(SearchScope.SUBTREE.getId());
        return ldapTemplate;
    }
}
