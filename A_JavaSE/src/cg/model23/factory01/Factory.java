package cg.model23.factory01;


public class Factory {

	public  Shape getShapeInstance(int type ) {

		switch (type) {
		case 0:return new point();
		case 1:return new Rectangle();
		case 2:return new Line();
		case 3:return new Circle();
		default:return new Circle();

		}
	}
}
