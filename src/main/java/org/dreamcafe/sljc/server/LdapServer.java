package org.dreamcafe.sljc.server;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import com.unboundid.ldif.LDIFReaderEntryTranslator;
import lombok.extern.slf4j.Slf4j;
import org.dreamcafe.sljc.config.LdapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class LdapServer {

    @Autowired
    private LdapProperties ldapProperties;

    private InMemoryDirectoryServer directoryServer;

    @PostConstruct
    public void startup() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ldapProperties.getLdifFilePathStr())) {
            InMemoryDirectoryServerConfig directoryServerConfig = new InMemoryDirectoryServerConfig(ldapProperties.getDefaultPartitionSuffix());
            directoryServerConfig.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("LDAP", ldapProperties.getPort()));
            directoryServerConfig.setEnforceSingleStructuralObjectClass(false);
            directoryServerConfig.setEnforceAttributeSyntaxCompliance(true);

            directoryServer = new InMemoryDirectoryServer(directoryServerConfig);

            LDIFReader reader = new LDIFReader(is, 0, new SljcLdifReaderEntryTranslator());
            directoryServer.importFromLDIF(true, reader);

            directoryServer.startListening();
        } catch (LDAPException | IOException ex) {
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

    private static class SljcLdifReaderEntryTranslator implements LDIFReaderEntryTranslator {

        @Override
        public Entry translate(final Entry original, final long firstLineNumber) throws LDIFException {
            if (original.hasAttribute("userPassword")) {
                log.info("Line {}, Password: {}", firstLineNumber, original.getAttribute("userPassword"));
            }
            return original;
        }
    }
}
