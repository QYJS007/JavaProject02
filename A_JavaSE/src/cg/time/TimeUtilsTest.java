package cg.time;

import java.sql.Time;
import java.util.Date;

public class TimeUtilsTest {
	public static void main(String[] args) {
		
		

		/**
		 * @描述：获取多少小时以后的时间
		 * @开发人员：sunbo
		 * @开发时间：2015年11月3日 上午10:00:07
		 * @param time
		 * @param h
		 * @return
		 */
		/*public static Time getAfterTimeHour(Time time,double h){ 
			Time newTime=new Time((long) (time.getTime()+h*60*60*1000));
			return newTime;
		}*/

		Date  now = new Date();
		//long time = now.getTime();
		Time newTime = new Time( now.getTime());
		
		System.out.println("newTime::"+ newTime);
		Time afterTimeHour = NovaDateUtil.getAfterTimeHour(newTime, 6D);
		
		System.out.println("afterTimeHour::"+ afterTimeHour);
	}
	

}
