package cg.model23.visitor3;

import java.util.Iterator;

public class ListVisitor extends Visitor {

	String currentDir = "";
	public void visit(File file) {
		System.out.println(currentDir+"/"+file);
	}

	public void visit(Directory directory) {
		System.out.println(currentDir+"/"+directory);
		String saveDir=currentDir;
		currentDir+=("/"+directory.getName());
		Iterator it=directory.iterator();
		while(it.hasNext()){
			Entry entry=(Entry)it.next();
			entry.accept(this);
		}
		currentDir=saveDir;
	}


} 
