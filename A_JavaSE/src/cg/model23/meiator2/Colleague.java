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
每个成员都必须知道Mediator，并且和 Mediator联系，而不是和其他成员联系。

至此，Mediator模式框架完成，可以发现Mediator模式规定不是很多，大体框架也比较简单，但实际使用起来就非常灵活。

Mediator模式在事件驱动类应用中比较多，例如界面设计GUI、聊天、消息传递等，在聊天应用中，需要有一个MessageMediator，专门负责request/reponse之间任务的调节。

MVC是J2EE的一个基本模式，View Controller是一种Mediator，它是Jsp和服务器上应用程序间的Mediator。
	 */

/*public abstract class Colleague {
    //持有一个调停者对象
    private Mediator mediator;
    *//**
     * 构造函数
     *//*
    public Colleague(Mediator mediator){
        this.mediator = mediator;
    }
    *//**
     * 获取当前同事类对应的调停者对象
     *//*
    public Mediator getMediator() {
        return mediator;
    }
    
}*/

public abstract class Colleague {
    //持有一个调停者对象
    private Mediator mediator;
    /**
     * 构造函数
     */
    public Colleague(Mediator mediator){
        this.mediator = mediator;
    }
    /**
     * 获取当前同事类对应的调停者对象
     */
    public Mediator getMediator() {
        return mediator;
    }

}