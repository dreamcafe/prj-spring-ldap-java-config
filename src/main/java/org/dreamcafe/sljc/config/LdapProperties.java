package org.dreamcafe.sljc.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dreamcafe.sljc.exception.PropertiesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
public class LdapProperties {
    public static final String PROP_LDAP_URL = "ldap.url";
    public static final String PROP_LDAP_DEFAULT_PARTITION_NAME = "ldap.default.partition.name";
    public static final String PROP_LDAP_DEFAULT_PARTITION_SUFFIX = "ldap.default.partition.suffix";
    public static final String PROP_LDAP_PRINCIPAL = "ldap.principal";
    public static final String PROP_LDAP_PASSWORD = "ldap.password";
    public static final String PROP_LDAP_LDIF_FILE_PATH = "ldap.ldif.file.path";
    public static final String PROP_LDAP_BASE = "ldap.base";

    private int port;

    @Value("${" + PROP_LDAP_URL + "}")
    private String url;

    @Value("${" + PROP_LDAP_DEFAULT_PARTITION_NAME + "}")
    private String defaultPartitionName;

    @Value("${" + PROP_LDAP_DEFAULT_PARTITION_SUFFIX + "}")
    private String defaultPartitionSuffix;

    @Value("${" + PROP_LDAP_PRINCIPAL + "}")
    private String principal;

    @Value("${" + PROP_LDAP_PASSWORD + "}")
    private String password;

    @Value("${" + PROP_LDAP_LDIF_FILE_PATH + "}")
    private String ldifFilePathStr;

    @Value("${" + PROP_LDAP_BASE + "}")
    private String base;

    @PostConstruct
    public void afterPropertiesSet() {
        if (StringUtils.isBlank(url)) {
            PropertiesException.asBlankValuePropertiesException(PROP_LDAP_URL);
        }

        String portStr = url.substring(url.lastIndexOf(":") + 1);

        try {
            port = Integer.valueOf(portStr);
        } catch (NumberFormatException ex) {
            log.error("Invalid port number '" + portStr + "'.");
            throw PropertiesException.asNumericValuePropertiesException(PROP_LDAP_URL);
        }
    }
}
