package cg.model23.visitor3;

import java.util.ArrayList;
import java.util.Iterator;


public class Directory extends Entry {

	String name;
	ArrayList entrys=new ArrayList();

	public Directory(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}

	public int getSize() {
		int size=0;
		Iterator it=entrys.iterator();
		while(it.hasNext()){
			size+=((Entry)it.next()).getSize();
		}
		return size;
	}

	public Entry add(Entry entry) {
		entrys.add(entry);
		return this;
	}

	public Iterator iterator() {
		return entrys.iterator();
	}

	public void printList(String prefix) {
		System.out.println(prefix+"/"+this);
		Iterator it=entrys.iterator();
		Entry entry;
		while(it.hasNext()){
			entry=(Entry)it.next();
			entry.printList(prefix+"/"+name);
		}
	}
	// ���ܷ���
	
	public void accept(Visitor visitor) {
		//  System.out.println("��ʼ�����ļ���:"+this);
		visitor.visit(this);
		//   System.out.println("���������ļ���:"+this);
		//   System.out.println();
	}

} 