package cg.model23.Observer.observe01;


public class q {
	public static void main(String[] args) {
		
		Publish publish = new Publish();
		Subscribe subscribe = new Subscribe(publish);
		publish.setData("kkksss");

		

	}
}
