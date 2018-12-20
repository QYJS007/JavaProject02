package cg.enums;

public class EnumTest {
	public static void main(String[] args) {
		//  TrafficLight trafficLight = new TrafficLight();
		System.out.println("sss");
	}
//	在JDK1.5 之前，我们定义常量都是： public static final.... 。现在好了，有了枚举，可以把相关的常量分组到一个枚举类型里，而且枚举提供了比常量更多的方法。 

	/*public enum Color {  
	  RED, GREEN, BLANK, YELLOW  
	} */
	
	
	//JDK1.6之前的switch语句只支持int,char,enum类型，使用枚举，能让我们的代码可读性更强。 

	enum Signal {  
	    GREEN, YELLOW, RED  
	}  
	public static class TrafficLight {
	    Signal color = Signal.RED;  
	    public void change() {  
	        switch (color) {  
	        case RED:  
	            color = Signal.GREEN;  
	            break;  
	        case YELLOW:  
	            color = Signal.RED;  
	            break;  
	        case GREEN:  
	            color = Signal.YELLOW;  
	            break;  
	        }  
	    }  
	}  
}

