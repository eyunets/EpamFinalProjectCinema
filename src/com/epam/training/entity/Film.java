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
public class Film {
	private int id;
	private String name;
	private String genre;
	private LocalDate year;
	private String description;
	private float rating;
	private float price;
}
