package org.dreamcafe.sljc.repository;

import lombok.extern.slf4j.Slf4j;
import org.dreamcafe.sljc.config.LdapConfig;
import org.dreamcafe.sljc.config.LdapTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapConfig.class, LdapTestConfig.class})
@Slf4j
public class AbstractRepositoryTests {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Test
    public void search() {
        ldapTemplate.search(query().attributes("cn").where("uid").is("000001"), (Attributes attributes) -> {
            return (String) attributes.get("cn").get();
        });
    }
}
