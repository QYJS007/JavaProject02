package cg.model23.factory01;

public class App {

	public static void main(String[] args) {
		
		
		String ss = "您好，有乘客申请开具发票。\n发票抬头：%s \n发票内容：%s \n发票金额：%s元 \n 请尽快登录您的企业后台进行处理。" ;
	String 	wetchatmessage = String.format(ss,"ss","111222","deesdsd");
	System.out.println(wetchatmessage);
	}
}
