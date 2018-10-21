package org.dreamcafe.sljc.repo;

import org.dreamcafe.sljc.domain.Person;

import java.util.List;

public interface PeopleRepo {

    List<String> listAllPeopleNames();

    List<String> listAllUserNames();

    List<String> listAllServiceAccountNames();

    List<Person> listAllPeople();

    List<Person> listAllUsers();

    List<Person> listAllServiceAccounts();

    Person findPersonByDn(String dn);

    List<Person> findPersonsByUid(String uid);

    Person findPersonByDnName(String uid);

    void save(Person person);
}
