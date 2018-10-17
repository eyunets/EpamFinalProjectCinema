package com.epam.training.entity;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Discount {

	private int id;
	private int userId;
	private int amount;
	private LocalDate stDate;
	private LocalDate enDate;
}