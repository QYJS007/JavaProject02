package cg.Perm;

import java.util.ArrayList;
import java.util.List;

public class A_Test {

/*
 * 证明了: jdk 8 , 没有Perm 方法区的了, 
 *  
 *  1.字符串常量由永久代转移到堆中。
	2.持久代已不存在，PermSize MaxPermSize参数已移除。（看图中最后两行）
 */
static String  base = "string";
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(2000);
		List<String> list = new ArrayList<String>();
		for (int i=0;i< Integer.MAX_VALUE;i++){
			String str = base + base;
			base = str;
			list.add(str.intern());
		}
	}
}
