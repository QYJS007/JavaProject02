package cg.enums.demo4;

import cg.enums.demo4.Meal.Food;
import cg.enums.demo4.Meal.Food.Coffee;




public class TypeOfFood {
	public static void main(String[] args) {
		/*Food food = Appetizer.SALAD;
		food = MainCourse.LASAGNE;
		food = Dessert.GELATO;
		food = Coffee.CAPPUCCINO;*/
		
		Meal ss = Meal.COFFEE;
		//System.out.println(ss);
		Meal meal[] =  Meal.values();
		
		for (int i = 0; i < meal.length; i++) {
			
			System.out.println(meal[i]);
		}
		// 应该是这样使用的 , 大类--下边的小类 
		Coffee aa = Meal.Food.Coffee.BLACK_COFFEE;
		System.out.println(aa);
	}
	
	/**
	 * 过这种方式可以很方便组织	上述的情景，
	 * 
	 * 同时确保每种具体类型的食物也属于Food，
	 * 
	 * 现在我们利用一个枚举嵌套枚举的方式，	
	 * 
	 * 把前面定义的菜谱存放到一个Meal菜单中，
	 * 
	 * 	通过这种方式就可以统一管理菜单的数据了。
	 */
} 



