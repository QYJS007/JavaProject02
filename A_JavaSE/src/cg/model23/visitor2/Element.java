package cg.model23.visitor2;

public abstract class Element {
    protected String name; //菜名
    protected float price; //价格
    protected int weight;    //份量
    
    abstract public void 供给(Visitor visitor);
    
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
}