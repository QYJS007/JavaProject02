package cg.model23.meiator2;

/*public class Colleague {
	private Mediator mediator;
	public Mediator getMediator() {
		return mediator;
	}
	public void setMediator( Mediator mediator ) {
		this.mediator = mediator;
	}}*/
	/*
ÿ����Ա������֪��Mediator�����Һ� Mediator��ϵ�������Ǻ�������Ա��ϵ��

���ˣ�Mediatorģʽ�����ɣ����Է���Mediatorģʽ�涨���Ǻܶ࣬������Ҳ�Ƚϼ򵥣���ʵ��ʹ�������ͷǳ���

Mediatorģʽ���¼�������Ӧ���бȽ϶࣬����������GUI�����졢��Ϣ���ݵȣ�������Ӧ���У���Ҫ��һ��MessageMediator��ר�Ÿ���request/reponse֮������ĵ��ڡ�

MVC��J2EE��һ������ģʽ��View Controller��һ��Mediator������Jsp�ͷ�������Ӧ�ó�����Mediator��
	 */

/*public abstract class Colleague {
    //����һ����ͣ�߶���
    private Mediator mediator;
    *//**
     * ���캯��
     *//*
    public Colleague(Mediator mediator){
        this.mediator = mediator;
    }
    *//**
     * ��ȡ��ǰͬ�����Ӧ�ĵ�ͣ�߶���
     *//*
    public Mediator getMediator() {
        return mediator;
    }
    
}*/

public abstract class Colleague {
    //����һ����ͣ�߶���
    private Mediator mediator;
    /**
     * ���캯��
     */
    public Colleague(Mediator mediator){
        this.mediator = mediator;
    }
    /**
     * ��ȡ��ǰͬ�����Ӧ�ĵ�ͣ�߶���
     */
    public Mediator getMediator() {
        return mediator;
    }

}