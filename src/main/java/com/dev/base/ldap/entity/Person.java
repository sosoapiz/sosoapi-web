package com.dev.base.ldap.entity;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

@Entry(objectClasses = { "inetOrgPerson", "top" })
public class Person {

	@Id
	private Name dn;

	@Attribute(name = "cn")
	@DnAttribute(value = "cn", index = 1)
	private String username;
	
	@Attribute(name = "mail")
	private String email;

	@Attribute(name = "givenName")
	private String firstName;
	
	@Attribute(name = "sn")
	private String lastName;
	
	@Attribute(name="mobile")
	private String mobile;
	
	@DnAttribute(value = "ou", index = 0)
	@Transient
	private String group;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Person [dn=" + dn + ", username=" + username + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", mobile=" + mobile + ", group=" + group + "]";
	}

}
