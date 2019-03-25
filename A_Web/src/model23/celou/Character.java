package model23.celou;

public abstract class Character { 
	public IAttackBehavior mIAttackBehavior;
	public ISpeedBehavior mISpeedBehavior;

	void move(){
		System.err.println("move");
	}
	void attack(){
		mIAttackBehavior.attack();
	}
	void speed(){
		mISpeedBehavior.speed();
	}

	public void setIAttackBehavior(IAttackBehavior IAttackBehavior) {
		mIAttackBehavior = IAttackBehavior;
	}

	public void setISpeedBehavior(ISpeedBehavior ISpeedBehavior) {
		mISpeedBehavior = ISpeedBehavior;
	}

	abstract void display();
}

class RedHeadZombie extends Character {//ºìÍ·½©Ê¬
	@Override
	void display() {
		System.err.println("My head is Red");
	}
}
//ÂÌÍ·½©Ê¬£º
class GreenHeadZombie extends Character {
	@Override
	void display() {
		System.err.println("My head is Green");
	}
}
