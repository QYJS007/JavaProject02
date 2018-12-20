package cg.enums;
/**“enum类中定义抽象方法”
 * enum类中定义抽象方法与常规抽象类一样，
 * enum类允许我们为其定义抽象方法，然后使每个枚举实例都实现该方法，
 * 以便产生不同的行为方式，注意abstract关键字对于枚举类来说并不是必须的如下：
 */
public enum EnumDemo3 {
	FIRST{
		@Override
		public String getInfo() {
			return "FIRST TIME";
		}
	},
	SECOND{
		@Override
		public String getInfo() {
			return "SECOND TIME";
		}
	};

	/**
	 * 定义抽象方法
	 * @return
	 */
	public abstract String getInfo();

	//测试
	public static void main(String[] args){
		System.out.println("F:"+EnumDemo3.FIRST.getInfo());
		System.out.println("S:"+EnumDemo3.SECOND.getInfo());
		/**
         输出结果:
         F:FIRST TIME
         S:SECOND TIME
		 */
	}
}
/**通过这种方式就可以轻而易举地定义每个枚举实例的不同行为方式。
 * 我们可能注意到，enum类的实例似乎表现出了多态的特性，
 * 
 * 可惜的是枚举类型的实例终究不能作为类型传递使用，就像下面的使用方式，编译器是不可能答应的：
 * 
 * 无法通过编译,毕竟EnumDemo3.FIRST是个实例对象    
 * public void text(EnumDemo3.FIRST instance){ }
 * 
 * 在枚举实例常量中定义抽象方法*/
