package cg.model23.visitor3;

import java.util.Iterator;


/**
 * 访问者模式的测试类
 * @author wangXgnaw
 *
 */
public class Test {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		Directory root=new Directory("根目录");

		Directory life=new Directory("我的生活");
		File eat=new File("吃火锅.txt",100);
		File sleep=new File("睡觉.html",100);
		File study=new File("学习.txt",100);
		life.add(eat);
		life.add(sleep);
		life.add(study);

		Directory work=new Directory("我的工作");
		File write=new File("写博客.doc",200);
		File paper=new File("写论文.html",200);
		File homework=new File("写家庭作业.docx",200);
		work.add(write);
		work.add(paper);
		work.add(homework);

		Directory relax=new Directory("我的休闲");
		File music=new File("听听音乐.js",200);
		File walk=new File("出去转转.psd",200);
		relax.add(music);
		relax.add(walk);

		Directory read=new Directory("我的阅读");
		File book=new File("学习书籍.psd",200);
		File novel=new File("娱乐小说.txt",200);
		read.add(book);
		read.add(novel);

		root.add(life);
		root.add(work);
		root.add(relax);
		root.add(read);

		root.accept(new ListVisitor());
		
		System.out.println("========================");
		FileVisitor visitor=new FileVisitor(".psd");
		root.accept(visitor);
		
		Iterator it = visitor.getFiles();
		while(it.hasNext()){
			System.out.println(it.next());
		}
/*
 * 那么串连起来，在Main中我们通过Directory或者File类型的对象调用accept（访问者）方法，
 * 接受访问者的访问，这是访问者和被访问者的第一次亲密接触，亲近对方就是为了获得对方的数据，然后才能对对方的数据进行使用，那么怎么拿到的呢？！
 * 
 * 我们看到了这句visitor.visit(this);这句话无疑是重要的，被调用者告诉访问者，我将我的内容this，全部给你了，以后访问者就可以对this所指代的被访问者的内容进行操作了，
 * 
 * 分为两类，	如果被访问者是File文件类型的，就会直接输出内容，到达叶子结点，访问结束；
 * 			如果是文件夹，那就非常有意思了，首先我们仍旧是让被访问者将自己的内容交给访问者visitor.visit(this);
 * 
 * 		，之后public void visit(Directory directory)被调用，
 * 			通过遍历的方式将属于这个文件夹下面的数据全部拿到	Iterator it=directory.iterator();
 * 
 * 		然后开始一个个的处理，怎么处理呢，继续访问属于这个文件夹下面对象的accept()方法使用entry.accept(this);，
 * 		
 * 		来将访问者交过去，交给谁？！肯定是给entry所指的对象，也就是文件夹里面的子文件夹或者文件，如果是文件的话，继续在自己的方法中调用visitor.visit(this);，
 * 
 * 		最终落实到调用 public void visit(File file)通过System.out.println(currentDir+"/"+file);访问结束，
 * 
 * 
 * 如果不是文件呢？
 * 
 * 		若为文件夹，则继续调用属于文件夹的方法，就这样不断地往下面查找，一直到遍历完文件夹下面的所有的元素，因此也是深度优先遍历。
 * 		
 * 		就这样通过压栈和出栈，我们完成了最终的遍历，最终的出口有两个，一个是访问文件，输出之后结束，另一个是遍历完文件夹，即使文件夹下面没有文件依旧结束。
 */
	}
}


/**
 * 
 * visitor模式的一大特点就是，结构是固定死的，你是不能改的，但你可以改一些结构的实现方式，即上述实例中的usb接口，
 * 你可以换成其他接口比如PCI……只要实现规定好的硬件接口interface类即可，这样一来，任凭怎么扩展，都不会修改到底层的结构，不会损坏“框架”了。
	不过，这也算是最大的缺点。就是不能扩展里面的内容，就上述内容来说，只能扩展接口，而不能扩展里面硬件，或者修改电脑硬件的实现方式！
 */
//访问者是什么？它表示作用于对象结构中元素的的操作，可以代替你去执行你需要的操作（间接访问），并且在不修改类的前提下，可以直接增加新的操作（直接增加新的方法）。



//访问者模式，首先，得定义一个访问者基类，它定义了所有元素需要的操作。比如说equip方法和kill方法，这里为了方便只定义了2个方法。
//其中的equipmentElement和equipmentElement类，是真实操作的元素，代表需要的具体实现。
