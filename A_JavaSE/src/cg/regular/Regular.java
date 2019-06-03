package cg.regular;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {

		/*		护照号码的格式：因私普通护照号码格式有:14/15+7位数,G+8位数；因公普通的是:P.+7位数；公务的是：S.+7位数 或者 S+8位数,以D开头的是外交护照.
		 * 
		 * D=diplomatic^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$
		 */	
		//Pattern pattern = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$");
		//pattern.matcher(input)
		//	System.out.println("140603199208265416");

	public static void main(String[] args) {
		// inputQQ();
		//show();
		//String reverseString = reverseString("123456");
		//System.out.println(reverseString);
	}
	


	/** 反转字符串(如：abcdef反转后为fedcba)  */
	public static String reverseString(String str){
		if(str.length()==0) return str;
		return reverseString(str.substring(1)) + str.charAt(0) ;
	}
	
	public static void show(){
		 // 定义字符串
        String s = "da jia ting wo shuo,jin tian yao xia yu,bu shang wan zi xi,gao xing bu?";
        // 规则 \\b表示单词边界，边界不允许出现单词字符
        String regex = "\\b\\w{3}\\b";
        // 把规则编译成模式对象
        Pattern p = Pattern.compile(regex);
        // 通过模式对象得到匹配器对象
        Matcher m = p.matcher(s);
        // 调用匹配器对象的功能
        // 通过find方法就是查找有没有满足条件的子串
        // public boolean find()
        while (m.find()) {
            System.out.println(m.group());
        }
        // 注意：一定要先find()，然后才能group()
        // IllegalStateException: No match found
        // String ss = m.group();
        // System.out.println(ss);

	}
	private static void inputHZ() {
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入您的HZ号码：");
		String str = sc.nextLine();
		boolean f = checkHZ(str);
		System.out.println("result:"+f);
	}
	public static boolean checkHZ(String hz) {
		String regex = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";
		boolean flag = hz.matches(regex);
		return flag;
		//return qq.matches("[1-9][0-9]{4,14}");
	}
	
	private static void inputQQ() {
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入您的QQ号码：");
		String str = sc.nextLine();
		boolean f = queckQQ(str);
		System.out.println("result:"+f);
	}
	public static boolean queckQQ(String qq) {
		String regex = "[1-9][0-9]{4,14}";
		boolean flag = qq.matches(regex);
		return flag;
		//return qq.matches("[1-9][0-9]{4,14}");
	}

}
