package cg.model23.visitor2;

import java.util.HashMap;

/**
 * 普通菜单
 * @author 南城草
 *
 */
public class Lunchbox {
    private HashMap<String, Element> elements;
    private float allPrice = 0;

    public Lunchbox() {
        elements = new HashMap();
    }

    public void Attach(Element element) {
        elements.put(element.getName(), element);
    }

    public void Detach(Element element) {
        elements.remove(element);
    }

    public Element getElemente(String name) {
        return elements.get(name);
    }

    public void Accept(Visitor visitor) {
        for (Element e : elements.values()) {
            e.供给(visitor);
            allPrice += e.getPrice() * e.getWeight();
        }
        System.out.print("总价:"+allPrice);
    }
}