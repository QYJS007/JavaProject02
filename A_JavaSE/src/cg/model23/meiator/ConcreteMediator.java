package cg.model23.meiator;

public class ConcreteMediator implements Mediator {
	/*
	 * Mediator定义：用一个中介对象来封装一系列关于对象交互行为。
为何使用Mediator模式/中介模式
各个对象之间的交互操作非常多，每个对象的行为操作都依赖彼此对方，修改一个对象的行为，同时会涉及到修改很多其他对象的行为，
如果使用Mediator模式，可以使各个对象间的耦合松散，只需关心和 Mediator的关系，使多对多的关系变成了一对多的关系，可以降低系统的复杂性，提高可修改扩展性。
	 */
	//假设当前有两个成员.
	/*private ConcreteColleagueA colleagueA = new ConcreteColleagueA();
	private ConcreteColleagueB colleagueB = new ConcreteColleagueB();*/
    //持有并维护同事A
    private ConcreteColleagueA colleagueA;
    //持有并维护同事B
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
         * 某一个同事类发生了变化，通常需要与其他同事交互
         * 具体协调相应的同事对象来实现协作行为
         */
    }
 
}
