package cg.model23.visitor2;

/**
 * 饭
 * @author 南城草
 *
 */
public class Meal extends Element {

	public Meal(int weight) {
		this();
		this.weight = weight;
		this.price = price*weight;
	}
	public Meal() {
		this.name = "饭";
		this.price = 2;
		this.weight = 1;
	}

	@Override
	public void 供给(Visitor visitor) {
		visitor.选菜(this);
	}

}



/**
 * 烧鸭
 * @author 南城草
 *
 */
class RoastDuck extends Element {

    public RoastDuck(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public RoastDuck() {
        this.name = "烧鸭";
        this.price = 8;
        this.weight = 1;
    }

    @Override
    public void 供给(Visitor visitor) {
        visitor.选菜(this);
    }

}
/**
 * 卷心菜
 * @author 南城草
 *
 */
 class Cabbage extends Element {

    public Cabbage(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public Cabbage() {
        this.name = "卷心菜";
        this.price = 2;
        this.weight = 1;
    }

    @Override
    public void 供给(Visitor visitor) {
        visitor.选菜(this);
    }

}
 /**
  * 叉烧
  * @author 南城草
  *
  */
class RoastPork extends Element {

     public RoastPork(int weight) {
         this();
         this.weight = weight;
         this.price = price*weight;
     }
     public RoastPork() {
         this.name = "叉烧";
         this.price = 8;
         this.weight = 1;
     }

     @Override
     public void 供给(Visitor visitor) {
         visitor.选菜(this);
     }

 }


/**
 * 汤
 * @author 南城草
 *
 */
class Soup extends Element {

    public Soup(int weight) {
        this();
        this.weight = weight;
        this.price = price*weight;
    }
    public Soup() {
        this.name = "汤";
        this.price = 3;
        this.weight = 1;
    }

    @Override
    public void 供给(Visitor visitor) {
        visitor.选菜(this);
    }

}