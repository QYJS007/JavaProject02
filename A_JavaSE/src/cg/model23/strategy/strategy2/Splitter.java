package cg.model23.strategy.strategy2;

import java.util.Arrays;

public class Splitter implements IStrategy {
	public String name() {
		return getClass().getSimpleName();
	}

	public String Arithmetic(String str) {
		return Arrays.toString(str.split(" "));
	}
}