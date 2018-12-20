package cg;

import java.util.HashMap;
import java.util.Map;

public class MyMath {
	public static void main(String[] args) {
		String idnum = "140603199208265416";
		System.out.println(MyMath.cleckIdNumber(idnum));
	}
	
	public static boolean cleckIdNumber(String ID){
		boolean flag = false;
		if(ID==null){
			return flag;
		}
		int IdNumlength = ID.length();
		if(IdNumlength!=18){
			return flag;
		}
		//验证码
		String validatecode = ID.substring(17,18);// 取最后一位
		//前17位称为本体码
		String selfcode = ID.substring(0,17);
		String code[]=new String[17];
		for(int i=0;i<17;i++)//遍历前17位
		{
			code[i] = selfcode.substring(i,i+1);
		}
		//加权因子公式：2的n-1次幂除以11取余数，n就是那个i，从右向左排列。
		int sum = 0;   //用于加权数求和
		for(int i=0;i<code.length;i++)
		{
			//计算该位加权因子
			double yi = Math.pow(2,i+1) %11;
			//得到对应数位上的数字
			int count = Integer.parseInt(code[code.length-i-1]);
			//加权求和 
			sum +=(count*yi);
		}
		//验证校验码是否正确
		String valdate = (String)validtable().get(String.valueOf(sum%11));
		if(valdate.equalsIgnoreCase(validatecode))
		{
			flag = true;
		}
		return flag;
	}
	/**
	 * 计算身份证数位数字加权因子
	 * digit表示数位
	 */
	public static int adjustmentfactor(int digit)
	{
		int sum = 1;
		for(int i=0;i<digit;i++)
		{
			sum=sum*2;
		}
		return sum;
	}
	//校验码对应表
	public static Map validtable()
	{
		Map map = new HashMap();
		String Var[] = {"1","0","X","9","8","7","6","5","4","3","2"};
		for(int i=0;i<Var.length;i++)
		{
			map.put(String.valueOf(i),String.valueOf(Var[i]));
		}
		return map;
	}

}
