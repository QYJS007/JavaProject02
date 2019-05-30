package cg.model23.visitor2;

import java.util.HashMap;

/**
 * ��ͨ�˵�
 * @author �ϳǲ�
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
            e.����(visitor);
            allPrice += e.getPrice() * e.getWeight();
        }
        System.out.print("�ܼ�:"+allPrice);
    }
}