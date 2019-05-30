package cg.model23.visitor2;
/**
 * 普通客户
 * @author 南城草
 *
 */
public class NormalVisitor implements Visitor {

	@Override
	public void 选菜(Element element) {
		Element food = ((Element) element);
		System.out.println(food.getName() +food.getWeight()+ "份!");
	}

}

