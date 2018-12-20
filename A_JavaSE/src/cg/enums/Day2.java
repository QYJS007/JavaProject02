package cg.enums;

public enum Day2 {
	/**
	 * 在前面的分析中，我们都是基于简单枚举类型的定义，也就是在定义枚举时只定义了枚举实例类型，并没定义方法或者成员变量，
	 * 
	 * 实际上使用关键字enum定义的枚举类，除了不能使用继承(因为编译器会自动为我们继承Enum抽象类而Java只支持单继承，
	 * 因此枚举类是无法手动实现继承的)，
	 * 
	 * 
	 * 可以把enum类当成常规类，也就是说我们可以向enum类中添加方法和变量，甚至是mian方法，下面就来感受一把。
	 */

	MONDAY("星期一"), TUESDAY("星期二"), WEDNESDAY("星期三"), THURSDAY("星期四"), FRIDAY(
			"星期五"), SATURDAY("星期六"), SUNDAY("星期日");// 记住要用分号结束

	private String desc;// 中文描述

	/**
	 * 私有构造,防止被外部调用
	 * 
	 * @param desc
	 */
	private Day2(String desc) {
		this.desc = desc;
	}

	/**
	 * 定义方法,返回描述,跟常规类的定义没区别
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	public static void main(String[] args) {
		for (Day2 day : Day2.values()) {
			System.out.println("name:" + day.name() + ",desc:" + day.getDesc());
		}
	}
	/**
	 * 输出结果: name:MONDAY,desc:星期一 name:TUESDAY,desc:星期二 name:WEDNESDAY,desc:星期三
	 * name:THURSDAY,desc:星期四 name:FRIDAY,desc:星期五 name:SATURDAY,desc:星期六
	 * name:SUNDAY,desc:星期日
	 */
}
/**
 * 从上述代码可知，在enum类中确实可以     像定义常规类一样声明变量或者成员方法。
 * 但是我们必须注意到，如果打算在enum类中定义方法，务必在声明完枚举实例后使用分号分开，倘若在枚举实例前定义任何方法，
 * 编译器都将会报错，无法编译通过，
 * 
 * 同时即使自定义了构造函数     且enum的定义结束，我们也永远无法手动调用构造函数创建枚举实例，     毕竟这事只能由编译器执行。
 */
