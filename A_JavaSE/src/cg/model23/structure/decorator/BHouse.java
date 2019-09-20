package cg.model23.structure.decorator;

public class BHouse extends House	implements DecoratorInteger {

	private House h;
	
	public BHouse(House h) {
		this.h =h;
	}

	@Override
	public void floor( House h) {
		 System.out.println("dalishi");

	}

	@Override
	public void ceil(House h ) {
		 System.out.println("diaoodoam");
		 h.ceil(h );
		 System.out.println("deng");

	}

}
