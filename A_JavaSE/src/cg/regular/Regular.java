package cg.regular;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {

		/*		���պ���ĸ�ʽ����˽��ͨ���պ����ʽ��:14/15+7λ��,G+8λ��������ͨ����:P.+7λ����������ǣ�S.+7λ�� ���� S+8λ��,��D��ͷ�����⽻����.
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
	


	/** ��ת�ַ���(�磺abcdef��ת��Ϊfedcba)  */
	public static String reverseString(String str){
		if(str.length()==0) return str;
		return reverseString(str.substring(1)) + str.charAt(0) ;
	}
	
	public static void show(){
		 // �����ַ���
        String s = "da jia ting wo shuo,jin tian yao xia yu,bu shang wan zi xi,gao xing bu?";
        // ���� \\b��ʾ���ʱ߽磬�߽粻������ֵ����ַ�
        String regex = "\\b\\w{3}\\b";
        // �ѹ�������ģʽ����
        Pattern p = Pattern.compile(regex);
        // ͨ��ģʽ����õ�ƥ��������
        Matcher m = p.matcher(s);
        // ����ƥ��������Ĺ���
        // ͨ��find�������ǲ�����û�������������Ӵ�
        // public boolean find()
        while (m.find()) {
            System.out.println(m.group());
        }
        // ע�⣺һ��Ҫ��find()��Ȼ�����group()
        // IllegalStateException: No match found
        // String ss = m.group();
        // System.out.println(ss);

	}
	private static void inputHZ() {
		Scanner sc = new Scanner(System.in);
		System.out.println("����������HZ���룺");
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
		System.out.println("����������QQ���룺");
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
