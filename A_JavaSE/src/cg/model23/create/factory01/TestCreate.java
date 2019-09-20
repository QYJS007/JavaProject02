package cg.model23.create.factory01;

import cg.model23.create.factory01.applicationclass.Shape;

public class TestCreate {

	public static void main(String[] args) {
		Shape shapeInstance = Factory.getShapeInstance(0);
		Shape shapeInstance2 = Factory.getShapeInstance(1);
		Shape shapeInstance3 = Factory.getShapeInstance(2);
		shapeInstance.display();
		shapeInstance2.display();
		shapeInstance3.display();
	}
}
