package cg.model23.visitor2;
/**
 * ��ͨ�ͻ�
 * @author �ϳǲ�
 *
 */
public class NormalVisitor implements Visitor {

	@Override
	public void ѡ��(Element element) {
		Element food = ((Element) element);
		System.out.println(food.getName() +food.getWeight()+ "��!");
	}

}

