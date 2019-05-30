package cg.model23.visitor2;

public abstract class Element {
    protected String name; //����
    protected float price; //�۸�
    protected int weight;    //����
    
    abstract public void ����(Visitor visitor);
    
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