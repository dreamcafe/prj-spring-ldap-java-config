package org.dreamcafe.sljc.repo.impl;

import org.dreamcafe.sljc.domain.Person;
import org.dreamcafe.sljc.repo.PeopleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class PeopleRepoImpl implements PeopleRepo {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public List<String> listAllPeopleNames() {
        LdapQuery query = query().base("ou=People")
                .attributes("cn")
                .where("objectclass").is("person");

        return ldapTemplate.search(query, (AttributesMapper<String>) attributes -> {
            return (String) attributes.get("cn").get();
        });
    }

    @Override
    public List<String> listAllUserNames() {
        LdapName name = LdapNameBuilder.newInstance().add("ou", "People").build();

        return ldapTemplate.search(name, "objectclass=person", SearchControls.ONELEVEL_SCOPE,
                (AttributesMapper<String>) attributes -> {
                    return (String) attributes.get("cn").get();
                });
    }

    @Override
    public List<String> listAllServiceAccountNames() {
        LdapName name = LdapNameBuilder.newInstance().add("ou", "People").add("ou", "Alternative Accounts").build();

        return ldapTemplate.search(name, "objectclass=person", SearchControls.ONELEVEL_SCOPE,
                (AttributesMapper<String>) attributes -> {
                    return (String) attributes.get("cn").get();
                });
    }

    @Override
    public List<Person> listAllPeople() {
        return ldapTemplate.search(query().where("objectclass").is("person"),
                new PersonAttributesMapper());
    }

    @Override
    public List<Person> listAllUsers() {
        return null;
    }

    @Override
    public List<Person> listAllServiceAccounts() {
        return null;
    }

    @Override
    public Person findPersonByDn(final String dn) {
        return ldapTemplate.lookup(dn, new PersonAttributesMapper());
    }

    @Override
    public List<Person> findPersonsByUid(final String uid) {
        LdapQuery query = query().base("ou=People")
                .attributes("uid", "cn", "sn")
                .where("objectclass").is("person")
                .and("uid").is(uid);

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    @Override
    public Person findPersonByDnName(final String uid) {
        LdapName dnName = LdapNameBuilder.newInstance()
                .add("ou", "People")
                .add("uid", uid)
                .build();
        return ldapTemplate.lookup(dnName, new PersonAttributesMapper());
    }

    @Override
    public void save(final Person person) {
        LdapName dnName = LdapNameBuilder.newInstance().add("ou", "People").add("uid", person.getUid()).build();

        Attributes attrs = new BasicAttributes();
        BasicAttribute objectClassAttr = new BasicAttribute("objectclass");
        objectClassAttr.add("top");
        objectClassAttr.add("person");
        objectClassAttr.add("organizationalPerson");
        objectClassAttr.add("inetOrgPerson");
        attrs.put(objectClassAttr);
        attrs.put("uid", person.getUid());
        attrs.put("cn", person.getFullName());
        attrs.put("sn", person.getSurName());
        attrs.put("userPassword", "password");

        ldapTemplate.bind(dnName, null, attrs);
    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {

        @Override
        public Person mapFromAttributes(Attributes attributes) throws NamingException {
            Person person = new Person();
            person.setUid((String) attributes.get("uid").get());
            person.setFullName((String) attributes.get("cn").get());
            person.setSurName((String) attributes.get("sn").get());
            return person;
        }
    }
}
