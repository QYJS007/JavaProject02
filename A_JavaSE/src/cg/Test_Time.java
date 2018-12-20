package cg;

import java.sql.Time;

public class Test_Time {

	public static void main(String[] args) {
		//11:10wewrwer
		Time afterTimeSecond = strToTime("11:10");
		System.out.println(afterTimeSecond );
		Time afterTimeHour = getAfterTimeHour(afterTimeSecond, 5);
		System.out.println(afterTimeHour);
		
		
		
		
	}
	 /**
	  * @描述：获取多少小时以后的时间
	  * @开发人员：sunbo
	  * @开发时间：2015年11月3日 上午10:00:07
	  * @param afterTimeSecond
	  * @param h
	  * @return
	  */
	 public static Time getAfterTimeHour(Time afterTimeSecond,double h){ 
	      Time newTime=new Time((long) (afterTimeSecond.getTime()+h*60*60*1000));
	      return newTime;
	  }
	 
	 /**
	  * @描述：获取多少小时以后的时间
	  * @开发人员：sunbo
	  * @开发时间：2015年11月3日 上午10:00:07
	  * @param time
	  * @param h
	  * @return
	  */
	 public static String getAfterTimeSecond(String timeStr, int sec){
		 String[] timeArr = timeStr.split(":");
		 if (timeArr.length == 2) {
			 timeStr += ":00";
		 }
		 Time time = Time.valueOf(timeStr);
		 System.out.println( "timeStr::::"+ timeStr);
	    Time newTime = new Time((long) (time.getTime() + sec * 1000));
	    return newTime.toString();
	 }
	 /**
	  * @描述：将字符串时间转换为Time类型的时间
	  * @开发人员：suqin
	  * @开发时间：2018年12月11日 上午10:00:07
	  * @param time
	  * @param h
	  * @return
	  */
	 public static Time strToTime(String timeStr){
		 String[] timeArr = timeStr.split(":");
		 if (timeArr.length == 2) {
			 timeStr += ":00";
		 }
		 Time time = Time.valueOf(timeStr);
		 return time;
	 }
}
