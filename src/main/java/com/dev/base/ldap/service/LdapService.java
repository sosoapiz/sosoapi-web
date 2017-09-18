package com.dev.base.ldap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import com.dev.base.ldap.dao.PersonRepository;
import com.dev.base.ldap.entity.Person;

@Service
public class LdapService {
	
	private final static Logger logger = LoggerFactory.getLogger(LdapService.class);

	@Autowired
	PersonRepository repository;
	@Autowired
	LdapTemplate ldapTemplate;

	public Person authenticate(String username, String password) {
		try {
			ldapTemplate.authenticate(LdapQueryBuilder.query().where("cn").is(username), //.or("mail").is(username), 
					password);
			Person person = repository.findByUsername(username);
			return person;
		} catch (Exception e) {
			String err = String.format("Ldap authenticate failed: '%s':'%s'", username, password);
			logger.trace(err);
			if (logger.isDebugEnabled()){
				e.printStackTrace();
			}
			throw e;
		}
	}
}
