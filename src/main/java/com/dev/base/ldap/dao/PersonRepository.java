package com.dev.base.ldap.dao;

import org.springframework.data.repository.CrudRepository;

import com.dev.base.ldap.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

	Person findByUsername(String username);
}
