package com.wing.framework.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类。
 * @author zhongzh
 */
public class DateUtil {
	
	public static Date now() {
		return (new Date());
	}
	
	/**
	 * 格式化日期
	 * @param date，待格式化日期
	 * @param pattern，格式化规则，默认为"yyyy-MM-dd"
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return "";
		if (pattern == null)
			pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return (sdf.format(date));
	}
	
	public static String formatDate(Date date) {
		return (formatDate(date, "yyyy-MM-dd"));
	}
	
	public static String formatDate() {
		return (formatDate(now(), "yyyy-MM-dd"));
	}
	
	public static String formatTime(Date date) {
		return (formatDate(date, "HH:mm:ss"));
	}

	public static String formatTime() {
		return (formatDate(now(), "HH:mm:ss"));
	}

	public static String formatDateTime(Date date) {
		return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String formatDateTime() {
		return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 以/yyyy/MM/dd规则格式化日期，若是月份数只有一位就显示一位，如/2013/1/15
	 * @param d
	 */
	public static String getDatePath(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		StringBuffer path = new StringBuffer();
		path.append("/");
		path.append(c.get(Calendar.YEAR));
		path.append("/");
		path.append(c.get(Calendar.MONTH) + 1);
		path.append("/");
		path.append(c.get(Calendar.DATE));
		return path.toString();
	}
	
	/**
	 * 依据给定的日期时间字符串，获取日期和时间
	 * @param date，必须是yyyy-MM-dd HH:mm:ss格式
	 * @return 日期字符串对应的日期时间
	 */
	public static Date parseDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ((datetime == null) || (datetime.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(datetime);
			} catch (ParseException e) {
				return parseDate(datetime);
			}
		}
	}
	
	/**
	 * 依据给定的日期字符串，获取日期，时间为00:00:00
	 * @param date，必须是yyyy-MM-dd格式，如2013-1-1
	 * @return 日期字符串对应的日期
	 */
	public static Date parseDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}
	/**
	 * 依据给定的日期字符串，获取日期，时间为00:00:00
	 * @param date，必须是yyyyMMdd格式，如2013011
	 * @return 日期字符串对于的日期
	 */
	public static Date parseDate2(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 截取日期时间中的日期，时间则设为00:00:00
	 * @param datetime
	 * @return
	 */
	public static Date parseDate(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (datetime == null) {
			return null;
		} else {
			try {
				return formatter.parse(formatter.format(datetime));
			} catch (ParseException e) {
				return null;
			}
		}
	}

    /**
     *
     * 格式化日期
     *
     * @param o
     * @return
     */
	public static String formatDate(Object o) {
		if (o == null)
			return "";
		if (o.getClass() == String.class)
			return o.toString();
		else if (o.getClass() == Date.class)
			return formatDate((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDate(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	public static String formatDateTime(Object o) {
		if (o.getClass() == String.class)
			return formatDateTime(o);
		else if (o.getClass() == Date.class)
			return formatDateTime((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDateTime(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	/**
	 * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
	 * 
	 * @param date
	 *            要加减前的时间，如果不传，则为当前日期
	 * @param field
	 *            时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
	 *            Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
	 * @param amount
	 *            按指定时间域加减的时间数量，正数为加，负数为减。
	 * @return 变动后的时间
	 */
	public static Date add(Date date, int field, int amount) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);

		return cal.getTime();
	}

	public static Date addMilliSecond(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	public static Date addSecond(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	public static Date addMiunte(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addHour(Date date, int amount) {
		return add(date, Calendar.HOUR, amount);
	}

	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DATE, amount);
	}

	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addYear(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date getDate() {
		return parseDate(formatDate());
	}

	public static Date getDateTime() {
		return parseDateTime(formatDateTime());
	}

	public static String getDateStrByTime(long time) {
		Date date = new Date(time);
		return date.toLocaleString().substring(0, date.toLocaleString().indexOf(" "));
	}

	public static String getDateStrByAnyDate(String str) {
		System.out.println(str);
		if (str.length() == 8 && str.indexOf("-") < 0 && str.indexOf("/") < 0) {
			return DateUtil.formatDate(DateUtil.parseDate2(str));
		}

		// str= 2011-01-01； 2011-1-1； 2011/1/1; 2011/01/01； 2011\1\1; 2011\01\01
		String[] tmp = null;
		if (str.indexOf("-") > 0)
			tmp = str.split("-");

		if (str.indexOf("/") > 0)
			tmp = str.split("/");

		// if(str.indexOf("\\") > 0)
		// tmp = str.split("\\");

		String year = StringUtils.trim(tmp[0]);
		String month = StringUtils.trim(tmp[1]);
		String day = StringUtils.trim(tmp[2]);

		if (month.length() == 1)
			month = "0" + month;

		if (day.length() == 1)
			day = "0" + day;

		return year + "-" + month + "-" + day;
	}

    public static String parseTimeToDate(long time){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        date.setTime(time*1000);
        String s = sdf.format(date);
        return s;
    }

    public static String  parseSecondToDate(long time){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(time * 1000);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(gc.getTime());
    }



	public static void main(String[] args) {
		Date parseDate = DateUtil.parseDate2("2013011");
		System.out.println(parseDate.toString());
		// System.out.println(DateUtil.getDateStrByAnyDate("2011-01-01"));
		// System.out.println(DateUtil.getDateStrByAnyDate("2011-1-1"));
		// System.out.println(DateUtil.getDateStrByAnyDate("2011/1/1"));
		// System.out.println(DateUtil.getDateStrByAnyDate("2011/01/01"));
		// System.out.println(DateUtil.getDateStrByAnyDate("20110101"));
		//System.out.println(DateUtil.getLastMonthDay());
		

		// System.out.println(DateUtil.getDateStrByAnyDate("2011\1\1"));
		// System.out.println(DateUtil.getDateStrByAnyDate("2011\01\01 "));
	}
}
