package cg.model23.strategy.strategy2;

public class UpCase implements IStrategy {
    public String name() {
        return getClass().getSimpleName();
    }

    public String Arithmetic(String str) {
        return str.toUpperCase();
    }
}