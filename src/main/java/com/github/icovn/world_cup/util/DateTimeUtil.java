package com.github.icovn.world_cup.util;

import com.github.icovn.util.DateUtil;
import java.util.Date;

public class DateTimeUtil {
  
  public static String getDateString(int date) {
    var matchDate = DateUtil.toDate(Integer.toString(date), "yyyyMMdd");
    return DateUtil.toString(matchDate, "dd/MM/yyyy");
  }
  
  public static String convertTimestampToString(long time) {
    var betDate = new Date(time);
    return DateUtil.toString(betDate, "HH:mm:ss dd/MM/yyyy");
  }
  
  public static long getStartTimeInTimestamp(int date, int startTime) {
    var hour = startTime/60;
    var minutes = startTime % 60;
    var dateAndTime = date + " " + hour + ":" + minutes;
    var matchDate = DateUtil.toDate(dateAndTime, "yyyyMMdd H:m");
    return matchDate.getTime();
  }
  
  public static String getTimeString(int startTime) {
    var hour = startTime/60;
    var minutes = startTime % 60;
    if (minutes == 0) {
      return "(" + hour + "h" + ")";
    }
    return "(" + hour + "h:" + minutes + ")";
  }
}
