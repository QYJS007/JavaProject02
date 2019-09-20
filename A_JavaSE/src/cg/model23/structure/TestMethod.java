package cg.model23.structure;

import cg.model23.structure.decorator.BHouse;
import cg.model23.structure.decorator.House;

public class TestMethod {

	public static void main(String[] args) {
		 House house = new House();
		 BHouse bHouse = new BHouse( house);
		 bHouse.ceil(house);

	}

}
