package cg.model23.visitor2;

/**
 * ��
 * @author �ϳǲ�
 *
 */
public class Meal extends Element {

	public Meal(int weight) {
		this();
		this.weight = weight;
		this.price = price*weight;
	}
	public Meal() {
		this.name = "��";
		this.price = 2;
		this.weight = 1;
	}

	@Override
	public void ����(Visitor visitor) {
		visitor.ѡ��(this);
	}

}



/**
 * ��Ѽ
 * @author �ϳǲ�
 *
 */
class RoastDuck extends Element {

    public RoastDuck(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public RoastDuck() {
        this.name = "��Ѽ";
        this.price = 8;
        this.weight = 1;
    }

    @Override
    public void ����(Visitor visitor) {
        visitor.ѡ��(this);
    }

}
/**
 * ���Ĳ�
 * @author �ϳǲ�
 *
 */
 class Cabbage extends Element {

    public Cabbage(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public Cabbage() {
        this.name = "���Ĳ�";
        this.price = 2;
        this.weight = 1;
    }

    @Override
    public void ����(Visitor visitor) {
        visitor.ѡ��(this);
    }

}
 /**
  * ����
  * @author �ϳǲ�
  *
  */
class RoastPork extends Element {

     public RoastPork(int weight) {
         this();
         this.weight = weight;
         this.price = price*weight;
     }
     public RoastPork() {
         this.name = "����";
         this.price = 8;
         this.weight = 1;
     }

     @Override
     public void ����(Visitor visitor) {
         visitor.ѡ��(this);
     }

 }


/**
 * ��
 * @author �ϳǲ�
 *
 */
class Soup extends Element {

    public Soup(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public Soup() {
        this.name = "��";
        this.price = 3;
        this.weight = 1;
    }

    @Override
    public void ����(Visitor visitor) {
        visitor.ѡ��(this);
    }

}