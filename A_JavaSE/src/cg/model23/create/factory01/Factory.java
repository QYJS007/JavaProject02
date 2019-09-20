package cg.model23.create.factory01;

import cg.model23.create.factory01.applicationclass.Circle;
import cg.model23.create.factory01.applicationclass.Line;
import cg.model23.create.factory01.applicationclass.Rectangle;
import cg.model23.create.factory01.applicationclass.Shape;
import cg.model23.create.factory01.applicationclass.point;


public class Factory {

	public static Shape getShapeInstance(int type ) {

		switch (type) {
		case 0:return new point();
		case 1:return new Rectangle();
		case 2:return new Line();
		case 3:return new Circle();
		default:return new Circle();

		}
	}
}
