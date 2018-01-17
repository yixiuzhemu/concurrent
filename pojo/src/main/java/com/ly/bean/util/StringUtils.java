package com.ly.bean.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {
	/**
	 * 按照指定的方式分割字符串,并且去除所含空格
	 * @param str 需要分割的字符串
	 * @param regx 分隔点
	 * @return String[] 返回分割后的字符串数组
	 */
	public static String[] splitStringTrim(String str, String regx) {

		String[] split = str.split(regx);
		String[] s = new String[split.length];
		for (int i = 0; i < split.length; i++) {
			s[i] = split[i].trim();
		}
		return s;
	}

	/**
	 * 将yyyy-MM-dd时间格式转换为ddMMMyy格式
	 * @param d
	 * @return String 返回指定格式的字符串
	 */
	public static String formatDateByyyyMMdd2ddMMMyy(String d) {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(d);
			SimpleDateFormat format = new SimpleDateFormat("ddMMMyy",
					new Locale("US"));
			String string = format.format(date);
			String s1 = string.substring(2, 5).toUpperCase();
			string = string.substring(0, 2) + s1 + string.substring(5);
			return string;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 格式化ddMMyyyy转换为yyyy-MM-dd
	 * @param time
	 * @return String 返回指定格式的字符串
	 */
	public static String formatByddMMMyyyy2yyyyMMdd(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy", Locale.ENGLISH);
		try {
			Date parse = sdf.parse(time);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			String format = sd.format(parse);
			return format;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 格式化yyyy-MM-dd hh:mm:ss 为 yyyy-MM-dd'T'hh:mm:ss
	 * @param date
	 * @return String 返回指定格式的字符串
	 */
	public static String formatDateAddT(String date) {
		Date d = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			d = sdf.parse(date);
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			date = s.format(d);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	/**
	 * Checks if a String is empty ("") or null.
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
}
