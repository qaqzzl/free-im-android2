package com.qaqzz.framework.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/30 22:41
 */
public class BirthdayToAgeConstellationShengXiaoUtil {

    private static String birthday;
    private static String ageStr;
    private static int age;
    //出生年、月、日
    private static int year;
    private static int month;
    private static int day;
    // birthday 时间戳
    public static String BirthdayToAge(String birthday) {
        birthday = birthday + "000";
        Date date = new Date( Long.parseLong( birthday ) );
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String Birthdate = format.format(date);
        stringToInt(Birthdate, "yyyy-MM-dd");
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);
        // 用当前年月日减去出生年月日
        int yearMinus = yearNow - year;
        int monthMinus = monthNow - month;
        int dayMinus = dayNow - day;
        age = yearMinus;// 先大致赋值
        if (yearMinus <= 0) {
            age = 0;
            ageStr = String.valueOf(age);
            return ageStr;
        }
        if (monthMinus < 0) {
            age = age - 1;
        } else if (monthMinus == 0) {
            if (dayMinus < 0) {
                age = age - 1;
            }
        }
        ageStr = String.valueOf(age);
        return ageStr;
    }

    /**
     * String类型转换成date类型
     * strTime: 要转换的string类型的时间，
     * formatType: 要转换的格式yyyy-MM-dd HH:mm:ss
     * //yyyy年MM月dd日 HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    private static Date stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date date;
            date = formatter.parse(strTime);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String类型转换为long类型
     * .............................
     * strTime为要转换的String类型时间
     * formatType时间格式
     * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * strTime的时间格式和formatType的时间格式必须相同
     */
    private static void stringToInt(String strTime, String formatType) {
        try {
            //String类型转换为date类型
            Calendar calendar = Calendar.getInstance();
            Date date = stringToDate(strTime, formatType);
            calendar.setTime(date);
            if (date == null) {
            } else {
                //date类型转成long类型
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } catch (Exception e) {
            Log.d("Exception birthday", e.toString());
        }
    }

    public static final String[] zodiacArray = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

    public static final String[] constellationArray = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
    public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
    /**
   * 根据日期获取生肖
   * @return
   */
    public static String dateToZodica(Calendar time) {
        return zodiacArray[time.get(Calendar.YEAR) % 12];
    }
    /**
   * 根据日期获取生肖
   * 
   * @return
   */
    public static String dateToZodica(String time) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(time);
            c.setTime(date);
            String zodica = dateToZodica(c);
//            System.out.println("生肖：" + zodica);
            return zodica;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * 根据日期获取星座
   * 
   * @param time
   * @return
   */
    public static String dateToConstellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }
    /**
   * 根据时间戳获取星座
   * 
   * @param time
   * @return
   */
    public static String TimeToConstellation(String time) {
        String getBirthdate = time + "000";
        Date date = new Date( Long.parseLong( getBirthdate ) );
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String Birthdate = format.format(date);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(Birthdate);
            c.setTime(date);
            String constellation = dateToConstellation(c);
//            System.out.println("星座：" + constellation);
            return constellation;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
