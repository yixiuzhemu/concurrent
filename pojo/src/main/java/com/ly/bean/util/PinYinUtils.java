package com.ly.bean.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtils {
	public static String getPinYin(String chinese){
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
		String pinyin1 = "";
		char[] charArray = chinese.toCharArray();
		for (char c : charArray) {
				if(String.valueOf(c).matches("[\u4e00-\u9fa5]+")){
					try {
						pinyin1 += PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0];
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
		return pinyin1;
	}
}
