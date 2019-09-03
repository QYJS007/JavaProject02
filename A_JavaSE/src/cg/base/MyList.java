package cg.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyList {   

	public static void main(String[] args) {
		 
			List<Integer> list = new ArrayList<Integer>();
			list.add(1);
			list.add(2);
			System.out.println(" 放入：是否为空 "+ list.isEmpty());
			List<Integer> list1 = new ArrayList<Integer>();
			list1.add(1);
			boolean remove = list.removeAll(list1);
			System.out.println( " 移除list：是否为空 "+ list.isEmpty());
			list.remove(new Integer(2));
			System.out.println("remove : 移除单个对象，是否为空： "+ remove);
			System.out.println( list.isEmpty());
			boolean remove2 = list.remove(null);
			
			 System.out.println("remove2: "+ remove2);
			 System.out.println( list.toString());
	 
	}

	
}