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
public class Ticket {
	private int id;
	private int userId;
	private int filmId;
	private float price;
	private int discount;
	private float payment;
	private LocalDate date;
}
