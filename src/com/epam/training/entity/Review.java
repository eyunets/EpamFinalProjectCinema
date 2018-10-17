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
public class Review {
	private int id;
	private int userId;
	private int filmId;
	private LocalDate date;
	private int mark;
	private String text;
}
