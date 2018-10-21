package org.dreamcafe.sljc.server;

import com.unboundid.ldap.listener.*;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import com.unboundid.ldif.LDIFReaderEntryTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dreamcafe.sljc.config.LdapProperties;
import org.dreamcafe.sljc.exception.PropertiesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class LdapServer {

    @Autowired
    private LdapProperties ldapProperties;

    private InMemoryDirectoryServer directoryServer;

    @PostConstruct
    public void startup() {
        if (StringUtils.isBlank(ldapProperties.getUrl())) {
            StringBuilder exMsg = new StringBuilder(200);
            exMsg.append("The LDAP URL is blank, which is invalid.").trimToSize();
            throw new PropertiesException(exMsg.toString());
        }

        String portStr = ldapProperties.getUrl().substring(ldapProperties.getUrl().lastIndexOf(":") + 1);

        Integer port = null;
        try {
            port = Integer.valueOf(portStr);
        } catch (NumberFormatException ex) {
            log.error("Invalid port string: {}", portStr);
            StringBuilder sb = new StringBuilder(200);
            sb.append("The port '").append(portStr).append(" specified in the URL is not a valid number.").trimToSize();
            throw new PropertiesException(sb.toString());
        }

        LDIFReader ldifReader = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            SaltedMessageDigestInMemoryPasswordEncoder passwordEncoder =
                    new SaltedMessageDigestInMemoryPasswordEncoder("encrypt",
                            HexPasswordEncoderOutputFormatter.getLowercaseInstance(),
                            messageDigest, 16, true, false);

            InMemoryDirectoryServerConfig directoryServerConfig = new InMemoryDirectoryServerConfig(
                    ldapProperties.getBaseDn());
            directoryServerConfig.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("LDAP", port));
            directoryServerConfig.setPasswordEncoders(passwordEncoder);
            directoryServerConfig.setEnforceSingleStructuralObjectClass(false);
            directoryServerConfig.setEnforceAttributeSyntaxCompliance(true);
            directoryServerConfig.setGenerateOperationalAttributes(true);

            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    ldapProperties.getLdifFilePath());
            if (null == is) {
                is = new FileInputStream(ldapProperties.getLdifFilePath());
            }
            LDIFReader reader = new LDIFReader(is, 0, (original, firstLineNumber) -> {
                if (original.hasAttribute("userPassword")) {
                    log.info("Line {}, Password: {}", firstLineNumber, original.getAttribute("userPassword"));
                }
                return original;
            });

            directoryServer = new InMemoryDirectoryServer(directoryServerConfig);
            directoryServer.importFromLDIF(true, reader);
            directoryServer.startListening();
        } catch (LDAPException | IOException | NoSuchAlgorithmException ex) {
            log.error("Failed to start up LDAP server.", ex);
            throw new IllegalStateException(ex);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (null != directoryServer) {
            directoryServer.shutDown(true);
        }
    }

}
