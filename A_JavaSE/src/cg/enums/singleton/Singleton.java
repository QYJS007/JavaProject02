package cg.enums.singleton;

/*public class AA {

}
 */

//枚举与单例模式

/*/////单例模式可以说是最常使用的设计模式了，它的作用是确保某个类只有一个实例，自行实例化并向整个系统提供这个实例。
 * 在实际应用中，线程池、缓存、日志对象、
 * 对话框对象常被设计成单例，
 * 
 * 总之，选择单例模式就是为了避免不一致状态，下面我们将会简单说明单例模式的几种主要编写方式，从而对比出使用枚举实现单例模式的优点。
 * 
 * 首先看看饿汉式的单例模式：
 */
/**
 * Created by wuzejian on 2017/5/9.
 * 饿汉式（基于classloder机制避免了多线程的同步问题）
 * 
 * public class SingletonHungry {

    private static SingletonHungry instance = new SingletonHungry();

    private SingletonHungry() {
    }

    public static SingletonHungry getInstance() {
        return instance;
    }
}*/
/*显然这种写法比较简单，但问题是无法做到延迟创建对象，事实上如果该单例类涉及资源较多，创建比较耗时间时，我们更希望它可以尽可能地延迟加载，
 * 
 * 从而减小初始化的负载，于是便有了如下的懒汉式单例：
 *//**
 * Created by wuzejian on 2017/5/9..
 * 懒汉式单例模式（适合多线程安全）
 * 
 * 
public class SingletonLazy {
    private static volatile SingletonLazy instance;

    private SingletonLazy() {
    }

    public static synchronized SingletonLazy getInstance() {
        if (instance == null) {
            instance = new SingletonLazy();
        }
        return instance;
    }
}*/

/*这种写法能够在多线程中很好的工作避免同步问题，同时也具备lazy loading机制，遗憾的是，由于synchronized的存在，效率很低，
 * 在单线程的情景下，完全可以去掉synchronized，为了兼顾效率与性能问题，改进后代码如下：

public class Singleton {
    private static volatile Singleton singleton = null;

    private Singleton(){}

    public static Singleton getSingleton(){
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }    
}*/



/*
 * 这种编写方式被称为“双重检查锁”，主要在getSingleton()方法中，进行两次null检查。
 * 
 * 这样可以极大提升并发度，进而提升性能。毕竟在单例中new的情况非常少，绝大多数都是可以并行的读操作，
 * 
 * 因此在加锁前多进行一次null检查就可以减少绝大多数的加锁操作，也就提高了执行效率。
 * 
 * 但是必须注意的是volatile关键字，该关键字有两层语义。
 * 				第一层语义是可见性，可见性是指在一个线程中对该变量的修改会马上由工作内存（Work Memory）写回主内存（Main Memory），所以其它线程会马上读取到已修改的值，
 * 							关于工作内存和主内存可简单理解为高速缓存（直接与CPU打交道）和主存（日常所说的内存条），注意工作内存是线程独享的，主存是线程共享的。
 * 
 * 				volatile的第二层语义是禁止指令重排序优化，我们写的代码（特别是多线程代码），由于编译器优化，在实际执行的时候可能与我们编写的顺序不同。
 * 						编译器只保证程序执行结果与源代码相同，却不保证实际指令的顺序与源代码相同，这在单线程并没什么问题，然而一旦引入多线程环境，这种乱序就可能导致严重问题。
 * 
 * 
 * 				volatile关键字就可以从语义上解决这个问题，值得关注的是volatile的禁止指令重排序优化功能在Java 1.5后才得以实现，因此1.5前的版本仍然是不安全的，即使使用了volatile关键字。
 * 
 * 				或许我们可以利用静态内部类来实现更安全的机制，静态内部类单例模式如下：
 *//**
 * Created by wuzejian on 2017/5/9.
 * 静态内部类
public class SingletonInner {
	private static class Holder {
		private static SingletonInner singleton = new SingletonInner();
	}

	private SingletonInner(){}

	public static SingletonInner getSingleton(){
		return Holder.singleton;
	}
}

	正如上述代码所展示的，我们把Singleton实例放到一个静态内部类中，这样可以避免了静态实例在Singleton类的加载阶段
	（类加载过程的其中一个阶段的，
			此时只创建了Class对象，关于Class对象可以看博主另外一篇博文， 深入理解Java类型信息(Class对象)与反射机制）就创建对象，

	毕竟静态变量初始化是在SingletonInner类初始化时触发的，并且由于静态内部类只会被加载一次，所以这种写法也是线程安全的。

	从上述4种单例模式的写法中，似乎也解决了效率与懒加载的问题，但是它们都有两个共同的缺点：

			序列化可能会破坏单例模式，比较每次反序列化一个序列化的对象实例时都会创建一个新的实例，解决方案如下：


  */

/** 
//测试例子(四种写解决方式雷同)
 * @author Administrator
 *
 */
/*public class Singleton implements java.io.Serializable {     
	public static Singleton INSTANCE = new Singleton();     
	protected Singleton() {     
	}  
	//反序列时直接返回当前INSTANCE
	private Object readResolve() {     
		return INSTANCE;     
	}    
}   */


// 使用反射强行调用私有构造器，解决方式可以修改构造器，让它在创建第二个实例的时候抛异常，如下：
public class Singleton implements java.io.Serializable {   
	public static Singleton INSTANCE = new Singleton();     
	private static volatile  boolean  flag = true;
	private Singleton(){
		if(flag){
			flag = false;   
		}else{
			throw new RuntimeException("The instance  already exists ！");
		}
	}
}
/// 如上所述，问题确实也得到了解决，但问题是我们为此付出了不少努力，即添加了不少代码，还应该注意到如果单例类维持了其他对象的状态时还需要使他们成为transient的对象，这种就更复杂了，
// 那有没有更简单更高效的呢？当然是有的，那就是枚举单例了，先来看看如何实现：
/**
 * Created by wuzejian on 2017/5/9.
 * 枚举单利
 */
/*public enum  SingletonEnum {
	INSTANCE;
	private String name;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
}*/


/*//代码相当简洁，我们也可以像常规类一样编写enum类，为其添加变量和方法，访问方式也更简单，使用SingletonEnum.INSTANCE进行访问，这样也就避免调用getInstance方法，
 * 
 * 更重要的是使用枚举单例的写法，我们完全不用考虑序列化和反射的问题。枚举序列化是由jvm保证的，每一个枚举类型和定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上，
 * Java做了特殊的规定：在序列化时Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
 * 
 * 同时，编译器是不允许任何对这种序列化机制的定制的并禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法，
 * 
 * 从而保证了枚举实例的唯一性，这里我们不妨再次看看Enum类的valueOf方法：
 */

