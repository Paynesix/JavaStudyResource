package stu.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateOne {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print(getIntervalDays(java.sql.Date.valueOf("2018-03-01"),java.sql.Date.valueOf("2018-01-01")));
	}

	  /**
     * 计算两个日期之间的间隔日期
     */
    public static int getIntervalDays(Date startDate, Date endDate) {
    	DateFormat df = DateFormat.getDateInstance();
    	GregorianCalendar cal1= new GregorianCalendar(),cal2= new GregorianCalendar(),calTmp= new GregorianCalendar();
		cal1.setTime(startDate);
		cal2.setTime(endDate);
		int flag=1;
		if (cal2.getTimeInMillis()<cal1.getTimeInMillis()){
			java.util.Date dt=cal2.getTime();
			cal2.setTime(cal1.getTime());
			cal1.setTime(dt);
			flag=-1;
		}
		int year1 = cal1.get(GregorianCalendar.YEAR),year2 = cal2.get(GregorianCalendar.YEAR),days=year2-year1;
		String firstday="-01-01",endday="-12-31";
		while(year2-year1>0){
			try {
				calTmp.setTime(df.parse(String.valueOf(year1)+endday));
				days+=calTmp.get(GregorianCalendar.DAY_OF_YEAR)-cal1.get(GregorianCalendar.DAY_OF_YEAR);
				cal1.setTime(df.parse(String.valueOf(++year1)+firstday));
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
		}
		days+=cal2.get(GregorianCalendar.DAY_OF_YEAR)-cal1.get(GregorianCalendar.DAY_OF_YEAR);
		days= days*flag;
		if (days==-0) days=0;
		return days;
    }
    
}
