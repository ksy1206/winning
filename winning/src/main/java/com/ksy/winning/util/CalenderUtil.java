package com.ksy.winning.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalenderUtil {

	/**
	 * 오늘 날짜
	 * @return
	 */
	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date todayDate = new Date();
		String today = sdf.format(todayDate);
		return today;
	}
}
