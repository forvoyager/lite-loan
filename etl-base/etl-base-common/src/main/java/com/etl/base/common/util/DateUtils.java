package com.etl.base.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * @summary 日期工具类
 */
public class DateUtils {

  /**
   * ZoneId: 时区ID，用来确定Instant和LocalDateTime互相转换的规则
   * Instant: 用来表示时间线上的一个点
   * LocalDate: 表示没有时区的日期, LocalDate是不可变并且线程安全的
   * LocalTime: 表示没有时区的时间, LocalTime是不可变并且线程安全的
   * LocalDateTime: 表示没有时区的日期时间, 偏移量是以UTC/格林威治时间为基准的, LocalDateTime是不可变并且线程安全的
   * Clock: 用于访问当前时刻、日期、时间，用到时区
   * Duration: 用秒和纳秒表示时间的数量
   *
   * Instant
   * 默认使用的是UTC(协调世界时 由原子钟提供)时间
   * 时间原点：格林威治时间(GMT)1970-01-01 00:00:00
   *
   */

  // 一天的秒数
  public static final int DAY_IN_SECOND = 86400;

  public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public static long currentTimeInSecond(){
    return Instant.now().getEpochSecond();
  }

  public static long currentDateInSecond(){
    return dateInSecond(currentTimeInSecond());
  }

  public static long dateInSecond(long timeInSecond){
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(timeInSecond * 1000);
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    return date.getTimeInMillis() / 1000;
  }

  public static long addMonth(long timeInSecond, int term){
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(timeInSecond * 1000);
    date.add(Calendar.MONTH, term);
    return date.getTimeInMillis() / 1000;
  }

  public static String parseTimestamp(long timeInSecond, String pattern){
    return new SimpleDateFormat(pattern).format(new Date(timeInSecond * 1000));
  }

  public static Date timeStrToDate(String timeStr, String pattern) throws ParseException {
    if(timeStr == null){ return null;}

    AssertUtils.notEmpty(timeStr, "时间字符串不合法");
    AssertUtils.notEmpty(pattern, "日期格式不合法");
    return new SimpleDateFormat(pattern).parse(timeStr);
  }

  public static Date timeStrToDate(String timeStr) throws ParseException {
    return timeStrToDate(timeStr, DEFAULT_PATTERN);
  }

  /**
   * date是否在[p1, p2]
   *
   * @param date
   * @param p1
   * @param p2
   * @return
   */
  public static boolean isDateBetween(Date date, Date p1, Date p2){
    if(date == null){ return false; }
    if(p1 != null && date.before(p1)){ return false; } // date在p1之前
    if(p2 != null && date.after(p2)){ return false; } // date在p2之后

    return true;
  }

  private DateUtils(){}
}
