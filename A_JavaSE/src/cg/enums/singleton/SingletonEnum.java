package cg.enums.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum  SingletonEnum {

	INSTANCE;
	private String name;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	/*	//代码相当简洁，我们也可以像常规类一样编写enum类，为其添加变量和方法，访问方式也更简单，
	 * 使用SingletonEnum.INSTANCE进行访问，这样也就避免调用getInstance方法，
	 * 
	 * 更重要的是使用枚举单例的写法，我们完全不用考虑序列化和反射的问题。
	 * 
	 * 枚举序列化是由jvm保证的，每一个枚举类型和定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上，
	 * 
	 * Java做了特殊的规定：在序列化时Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
	 * 
	 * 同时，编译器是不允许任何对这种序列化机制的定制的并禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法，
	 * 从而保证了枚举实例的唯一性，这里我们不妨再次看看Enum类的valueOf方法：
	 */


	/*public static <T extends Enum<T>> T valueOf(Class<T> enumType,
			String name) {
		T result = enumType.enumConstantDirectory().get(name);
		if (result != null)
			return result;
		if (name == null)
			throw new NullPointerException("Name is null");
		throw new IllegalArgumentException(
				"No enum constant " + enumType.getCanonicalName() + "." + name);
	}*/


	/*	实际上通过调用enumType(Class对象的引用)的enumConstantDirectory方法获取到的是一个Map集合，
	 * 在该集合中存放了以枚举name为key和以枚举实例变量为value的Key&Value数据，因此通过name的值就可以获取到枚举实例，
	 * 
	 * 看看enumConstantDirectory方法源码：
	 */

	/*Map<String, T> enumConstantDirectory() {
	        if (enumConstantDirectory == null) {
	            //getEnumConstantsShared最终通过反射调用枚举类的values方法
	            T[] universe = getEnumConstantsShared();
	            if (universe == null)
	                throw new IllegalArgumentException(
	                    getName() + " is not an enum type");
	            Map<String, T> m = new HashMap<>(2 * universe.length);
	            //map存放了当前enum类的所有枚举实例变量，以name为key值
	            for (T constant : universe)
	                m.put(((Enum<?>)constant).name(), constant);
	            enumConstantDirectory = m;
	        }
	        return enumConstantDirectory;
	    }
	    private volatile transient Map<String, T> enumConstantDirectory = null
	 */


	/*	到这里我们也就可以看出枚举序列化确实不会重新创建新实例，jvm保证了每个枚举实例变量的唯一性。
	 * 
	 * 再来看看反射到底能不能创建枚举，下面试图通过反射获取构造器并创建枚举
	 */	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		 //获取枚举类的构造函数(前面的源码已分析过)
		 Constructor<SingletonEnum> constructor=SingletonEnum.class.getDeclaredConstructor(String.class,int.class);
		 constructor.setAccessible(true);
		 //创建枚举
		 SingletonEnum singleton=constructor.newInstance("otherInstance",9);
		 System.out.println(singleton);
		 
		 /*Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
			at java.lang.reflect.Constructor.newInstance(Constructor.java:520)
			at cg.enums.singleton.SingletonEnum.main(SingletonEnum.java:73)


			显然告诉我们不能使用反射创建枚举类，这是为什么呢？不妨看看newInstance方法源码：
			 public T newInstance(Object ... initargs)
			        throws InstantiationException, IllegalAccessException,  IllegalArgumentException, InvocationTargetException {
			        if (!override) {
			            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
			                Class<?> caller = Reflection.getCallerClass();
			                checkAccess(caller, clazz, null, modifiers);
			            }
			        }
			        
			        
			        
			        //这里判断Modifier.ENUM是不是枚举修饰符，如果是就抛异常
			        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
			            throw new IllegalArgumentException("Cannot reflectively create enum objects");
			        ConstructorAccessor ca = constructorAccessor;   // read volatile
			        if (ca == null) {
			            ca = acquireConstructorAccessor();
			        }
			        @SuppressWarnings("unchecked")
			        T inst = (T) ca.newInstance(initargs);
			        return inst;
			    }
		源码很了然，确实无法使用反射创建枚举实例，也就是说明了创建枚举实例只有编译器能够做到而已。
		
		显然枚举单例模式确实是很不错的选择，因此我们推荐使用它。
		但是这总不是万能的，对于android平台这个可能未必是最好的选择，在android开发中，内存优化是个大块头，而使用枚举时占用的内存常常是静态变量的两倍还多，
		
		因此android官方在内存优化方面给出的建议是尽量避免在android中使用enum。
		
		但是不管如何，关于单例，我们总是应该记住：线程安全，延迟加载，序列化与反序列化安全，反射安全是很重重要的。

		  */	 


	 }
}
