package cg.model23.meiator;


public class ConcreteColleagueA extends Colleague {
 
    public ConcreteColleagueA(Mediator mediator) {
        super(mediator);
    }
    /**
     * ʾ�ⷽ����ִ��ĳЩ����
     */
    public void operation(){
        //����Ҫ������ͬ��ͨ�ŵ�ʱ��֪ͨ��ͣ�߶���
        getMediator().changed(this);
    }
}
//https://blog.csdn.net/janice0529/article/details/41685175