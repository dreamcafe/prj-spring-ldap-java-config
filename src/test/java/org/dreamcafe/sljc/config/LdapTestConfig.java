package org.dreamcafe.sljc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ldap.test.unboundid.TestContextSourceFactoryBean;

@Configuration
public class LdapTestConfig {

    @Bean
    public TestContextSourceFactoryBean testContextSource(LdapProperties ldapProperties) {
        TestContextSourceFactoryBean factoryBean = new TestContextSourceFactoryBean();
        factoryBean.setDefaultPartitionName(ldapProperties.getDefaultPartitionName());
        factoryBean.setDefaultPartitionSuffix(ldapProperties.getDefaultPartitionSuffix());
        factoryBean.setPrincipal(ldapProperties.getPrincipal());
        factoryBean.setPassword(ldapProperties.getPassword());
        factoryBean.setLdifFile(new ClassPathResource(ldapProperties.getLdifFilePathStr()));
        factoryBean.setPort(ldapProperties.getPort());

        return factoryBean;
    }
}
