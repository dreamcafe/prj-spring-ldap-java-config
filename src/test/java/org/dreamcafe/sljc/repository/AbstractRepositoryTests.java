package org.dreamcafe.sljc.repository;

import lombok.extern.slf4j.Slf4j;
import org.dreamcafe.sljc.config.LdapConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.directory.Attributes;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapConfig.class})
@Slf4j
public class AbstractRepositoryTests {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Test
    public void search() {
        List<String> cns = ldapTemplate.search(query().attributes("cn").where("uid").is("000001"), (Attributes attributes) -> {
            return (String) attributes.get("cn").get();
        });

        cns.forEach(cn -> {log.info("Name: {}", cn);});
    }
}
