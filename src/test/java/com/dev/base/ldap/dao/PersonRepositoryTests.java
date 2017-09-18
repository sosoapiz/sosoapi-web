package com.dev.base.ldap.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.dev.base.ldap.entity.Person;
import com.dev.base.test.BaseTest;

public class PersonRepositoryTests extends BaseTest {

	@Autowired
	PersonRepository repository;

	@Autowired
	LdapTemplate ldapTemplate;

	@Test
	public void testReadAll() {
		List<Person> persons = (List<Person>) repository.findAll();
		for (Person person : persons) {
			System.out.println(">>> " + person);
		}
		assertThat(persons.isEmpty(), is(false));
	}

	@Test
	public void testAuth() {
		if (null != ldapTemplate) {
			ldapTemplate.authenticate(LdapQueryBuilder.query().where("cn").is("yintao"), "yintao");
		}
	}

	@Test
	public void testReadOne() {
		Person person = repository.findByUsername("yintao");
		assertThat(person.getUsername(), is("yintao"));
	}
}
