package com.epam.training.util;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class SqlDateConverter {
	public Date toSQLDate(LocalDate localDate) {
		LocalDateTime localDateTime = localDate.atStartOfDay();
		return new Date(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	public LocalDate toLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
