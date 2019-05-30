package cg.model23.visitor3;

import java.util.Iterator;


public class File extends Entry {

	private String name;
	private int size;
	public File(String name,int size){
		this.name=name;
		this.size=size;
	}
	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public void printList(String prefix) {
		System.out.println(prefix+"/"+this);
	}
	public void accept(Visitor visitor) {
		//  System.out.println("��ʼ�����ļ�:"+this);
		visitor.visit(this);
		// System.out.println("���������ļ�:"+this);
		// System.out.println();
	}

} 

