package com.fmcna.fhpckd.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author vidhishanandhikonda
 *
 */
public class DateUtil {
	public static Date getOldestRefDate() {
		Calendar oldestRefDate = Calendar.getInstance();

		// Hard code to Jan 1, 1970
		oldestRefDate.set(1970, 0, 01, 0, 0);
		
		return oldestRefDate.getTime();
	}

	/**
	 * get the input String upto Max Length if it is exceeded the limit
	 */
	public static String getTextUptoCharacters(String inputText, int expectedLenth) {
		String outputText = null;

		if (inputText != null) {
			outputText = (inputText.length() > expectedLenth) ? inputText.substring(0, expectedLenth) : inputText;
		}
		return outputText;
	}
}


