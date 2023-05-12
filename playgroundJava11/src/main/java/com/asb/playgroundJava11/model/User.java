package com.asb.playgroundJava11.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String password;

}
