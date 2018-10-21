package org.dreamcafe.sljc.repository;

import org.dreamcafe.sljc.domain.Person;
import org.dreamcafe.sljc.repo.PeopleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PeopleRepoTest extends AbstractRepoTests {

    @Autowired
    private PeopleRepo peopleRepo;

    @Test
    public void listAllPeopleNames() {
        List<String> names = peopleRepo.listAllPeopleNames();
        assertThat(names.size()).isEqualTo(4);
        assertThat(names).contains("Will Smith", "Alan Parker", "John Flint", "Service Account 001");
    }

    @Test
    public void listAllUserNames() {
        List<String> names = peopleRepo.listAllUserNames();
        assertThat(names.size()).isEqualTo(3);
        assertThat(names).contains("Will Smith", "Alan Parker", "John Flint");
    }

    @Test
    public void listAllAlternativeAccountNames() {
        List<String> names = peopleRepo.listAllServiceAccountNames();
        assertThat(names.size()).isEqualTo(1);
        assertThat(names).contains("Service Account 001");

    }

    @Test
    public void listAllPersons() {
        List<Person> allPersons = peopleRepo.listAllPeople();
        assertThat(allPersons.size()).isGreaterThan(0);
    }

    @Test
    public void findPersonByDn() {
        Person person = peopleRepo.findPersonByDn("uid=\"000001\",ou=\"People\"");
        assertThat(person).isNotNull();
    }

    @Test
    public void findPersonsByUid() {
        String uid = "000001";
        List<Person> persons = peopleRepo.findPersonsByUid(uid);
        assertThat(persons).isNotNull();
        assertThat(persons.size()).isEqualTo(1);
    }

    @Test
    public void findPersonByDnName() {
        String uid = "000001";

        Person person = peopleRepo.findPersonByDnName(uid);
        assertThat(person).isNotNull();
    }

    public void save() {
        Person
    }
}
