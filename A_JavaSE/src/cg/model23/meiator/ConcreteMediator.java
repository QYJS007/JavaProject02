package cg.model23.meiator;

public class ConcreteMediator implements Mediator {
	/*
	 * Mediator���壺��һ���н��������װһϵ�й��ڶ��󽻻���Ϊ��
Ϊ��ʹ��Mediatorģʽ/�н�ģʽ
��������֮��Ľ��������ǳ��࣬ÿ���������Ϊ�����������˴˶Է����޸�һ���������Ϊ��ͬʱ���漰���޸ĺܶ������������Ϊ��
���ʹ��Mediatorģʽ������ʹ���������������ɢ��ֻ����ĺ� Mediator�Ĺ�ϵ��ʹ��Զ�Ĺ�ϵ�����һ�Զ�Ĺ�ϵ�����Խ���ϵͳ�ĸ����ԣ���߿��޸���չ�ԡ�
	 */
	//���赱ǰ��������Ա.
	/*private ConcreteColleagueA colleagueA = new ConcreteColleagueA();
	private ConcreteColleagueB colleagueB = new ConcreteColleagueB();*/
    //���в�ά��ͬ��A
    private ConcreteColleagueA colleagueA;
    //���в�ά��ͬ��B
    private ConcreteColleagueB colleagueB;    
    
    public void setColleagueA(ConcreteColleagueA colleagueA) {
        this.colleagueA = colleagueA;
    }
 
    public void setColleagueB(ConcreteColleagueB colleagueB) {
        this.colleagueB = colleagueB;
    }
 
    @Override
    public void changed(Colleague c) {
        /**
         * ĳһ��ͬ���෢���˱仯��ͨ����Ҫ������ͬ�½���
         * ����Э����Ӧ��ͬ�¶�����ʵ��Э����Ϊ
         */
    }
 
}
