package com.epam.training.entity;

import java.sql.Time;
import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class News {

	private int id;
	private LocalDate date;
	private Time time;
	private String title;
	private String text;

}
