package cg.model23.strategy.strategy2;
/**
 * 策略模式通过组合的方式实现具体算法
 * 其要执行的算法不变，封装到一个类(Context)中
 */
public class Context {

    private IStrategy mStrategy;

    /**
     * 将抽象接口的实现传递给组合对象
     * @param strategy
     */
    public Context(IStrategy strategy){
        this.mStrategy = strategy;
    }

    /**
     * 封装逻辑(算法)
     * @param s
     */
    public void doAction(String s){
        System.out.println(mStrategy.name());
        System.out.println(this.mStrategy.Arithmetic(s));
    }
}