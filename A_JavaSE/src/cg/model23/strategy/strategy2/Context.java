package cg.model23.strategy.strategy2;
/**
 * ����ģʽͨ����ϵķ�ʽʵ�־����㷨
 * ��Ҫִ�е��㷨���䣬��װ��һ����(Context)��
 */
public class Context {

    private IStrategy mStrategy;

    /**
     * ������ӿڵ�ʵ�ִ��ݸ���϶���
     * @param strategy
     */
    public Context(IStrategy strategy){
        this.mStrategy = strategy;
    }

    /**
     * ��װ�߼�(�㷨)
     * @param s
     */
    public void doAction(String s){
        System.out.println(mStrategy.name());
        System.out.println(this.mStrategy.Arithmetic(s));
    }
}