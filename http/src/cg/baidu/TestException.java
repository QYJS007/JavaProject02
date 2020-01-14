package cg.baidu;

import java.util.Date;

public class TestException {

	public static void main(String[] args) {
		
		Integer   num = null;
		try {
			num = getNum(num);
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		} 
		System.out.println(num  );
	}

	private static Integer getNum(Integer num) throws Exception {
		//Integer num;
		int a = 100 ; 
		if(num !=null){
			num = a/num ;
		}else{
			//throw new Exception("num的值有误 ，的结果不正确");
			//return null;
		}
		
		//Date date = new Date();
		//date.getDay();
		
		/* String ss ="' ";
		 String[] split = ss.split(",");
		String aaa=  split[1] ;*/
		try {
			a =1/0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new Exception(e);
		}
		
		
		return num;
	}
}
