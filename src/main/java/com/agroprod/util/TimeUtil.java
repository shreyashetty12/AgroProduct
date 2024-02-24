package com.agroprod.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

	public String getCurrTime() {
		Date startTime = Calendar.getInstance().getTime();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String currentTime = simpleDateFormat.format(startTime);
		System.out.println(currentTime);

		return currentTime;

	}

	public boolean getTimeDifference(String acceptedTime) {

		String currentTime = getCurrTime();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime createdTime = LocalDateTime.parse(acceptedTime, formatter);
		LocalDateTime currTime = LocalDateTime.parse(currentTime, formatter);

		long diffInMinutes = java.time.Duration.between(createdTime, currTime).toMinutes();

		return diffInMinutes >= 1440;

	}

}
