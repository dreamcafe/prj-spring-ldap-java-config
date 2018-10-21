package org.dreamcafe.sljc.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Getter
public class LdapProperties {
    public static final String PROP_LDAP_URL = "ldap.url";
    public static final String PROP_LDAP_BASE_DN = "ldap.base.dn";
    public static final String PROP_LDAP_PRINCIPAL = "ldap.principal";
    public static final String PROP_LDAP_PASSWORD = "ldap.password";
    public static final String PROP_LDAP_LDIF_FILE_PATH = "ldap.ldif.file.path";

    @Value("${" + PROP_LDAP_URL + "}")
    private String url;

    @Value("${" + PROP_LDAP_BASE_DN + "}")
    private String baseDn;

    @Value("${" + PROP_LDAP_PRINCIPAL + "}")
    private String principal;

    @Value("${" + PROP_LDAP_PASSWORD + "}")
    private String password;

    @Value("${" + PROP_LDAP_LDIF_FILE_PATH + "}")
    private String ldifFilePath;
}
