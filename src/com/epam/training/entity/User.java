package com.epam.training.entity;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {
	private int id;
	private String name;
	private String surname;
	private String password;
	private String type;
	private String email;
	private BigDecimal balance;
}
