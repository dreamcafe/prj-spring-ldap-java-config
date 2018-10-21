package org.dreamcafe.sljc.repository;

import lombok.extern.slf4j.Slf4j;
import org.dreamcafe.sljc.config.LdapConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.directory.Attributes;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapConfig.class})
@Slf4j
public abstract class AbstractRepoTests {

    @Autowired
    private LdapTemplate ldapTemplate;
}
