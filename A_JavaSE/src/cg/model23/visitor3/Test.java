package cg.model23.visitor3;

import java.util.Iterator;


/**
 * ������ģʽ�Ĳ�����
 * @author wangXgnaw
 *
 */
public class Test {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		Directory root=new Directory("��Ŀ¼");

		Directory life=new Directory("�ҵ�����");
		File eat=new File("�Ի��.txt",100);
		File sleep=new File("˯��.html",100);
		File study=new File("ѧϰ.txt",100);
		life.add(eat);
		life.add(sleep);
		life.add(study);

		Directory work=new Directory("�ҵĹ���");
		File write=new File("д����.doc",200);
		File paper=new File("д����.html",200);
		File homework=new File("д��ͥ��ҵ.docx",200);
		work.add(write);
		work.add(paper);
		work.add(homework);

		Directory relax=new Directory("�ҵ�����");
		File music=new File("��������.js",200);
		File walk=new File("��ȥתת.psd",200);
		relax.add(music);
		relax.add(walk);

		Directory read=new Directory("�ҵ��Ķ�");
		File book=new File("ѧϰ�鼮.psd",200);
		File novel=new File("����С˵.txt",200);
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
 * ��ô������������Main������ͨ��Directory����File���͵Ķ������accept�������ߣ�������
 * ���ܷ����ߵķ��ʣ����Ƿ����ߺͱ������ߵĵ�һ�����ܽӴ����׽��Է�����Ϊ�˻�öԷ������ݣ�Ȼ����ܶԶԷ������ݽ���ʹ�ã���ô��ô�õ����أ���
 * 
 * ���ǿ��������visitor.visit(this);��仰��������Ҫ�ģ��������߸��߷����ߣ��ҽ��ҵ�����this��ȫ�������ˣ��Ժ�����߾Ϳ��Զ�this��ָ���ı������ߵ����ݽ��в����ˣ�
 * 
 * ��Ϊ���࣬	�������������File�ļ����͵ģ��ͻ�ֱ��������ݣ�����Ҷ�ӽ�㣬���ʽ�����
 * 			������ļ��У��Ǿͷǳ�����˼�ˣ����������Ծ����ñ������߽��Լ������ݽ���������visitor.visit(this);
 * 
 * 		��֮��public void visit(Directory directory)�����ã�
 * 			ͨ�������ķ�ʽ����������ļ������������ȫ���õ�	Iterator it=directory.iterator();
 * 
 * 		Ȼ��ʼһ�����Ĵ�����ô�����أ�����������������ļ�����������accept()����ʹ��entry.accept(this);��
 * 		
 * 		���������߽���ȥ������˭�����϶��Ǹ�entry��ָ�Ķ���Ҳ�����ļ�����������ļ��л����ļ���������ļ��Ļ����������Լ��ķ����е���visitor.visit(this);��
 * 
 * 		������ʵ������ public void visit(File file)ͨ��System.out.println(currentDir+"/"+file);���ʽ�����
 * 
 * 
 * ��������ļ��أ�
 * 
 * 		��Ϊ�ļ��У���������������ļ��еķ��������������ϵ���������ң�һֱ���������ļ�����������е�Ԫ�أ����Ҳ��������ȱ�����
 * 		
 * 		������ͨ��ѹջ�ͳ�ջ��������������յı��������յĳ�����������һ���Ƿ����ļ������֮���������һ���Ǳ������ļ��У���ʹ�ļ�������û���ļ����ɽ�����
 */
	}
}


/**
 * 
 * visitorģʽ��һ���ص���ǣ��ṹ�ǹ̶����ģ����ǲ��ܸĵģ�������Ը�һЩ�ṹ��ʵ�ַ�ʽ��������ʵ���е�usb�ӿڣ�
 * ����Ի��������ӿڱ���PCI����ֻҪʵ�ֹ涨�õ�Ӳ���ӿ�interface�༴�ɣ�����һ������ƾ��ô��չ���������޸ĵ��ײ�Ľṹ�������𻵡���ܡ��ˡ�
	��������Ҳ��������ȱ�㡣���ǲ�����չ��������ݣ�������������˵��ֻ����չ�ӿڣ���������չ����Ӳ���������޸ĵ���Ӳ����ʵ�ַ�ʽ��
 */
//��������ʲô������ʾ�����ڶ���ṹ��Ԫ�صĵĲ��������Դ�����ȥִ������Ҫ�Ĳ�������ӷ��ʣ��������ڲ��޸����ǰ���£�����ֱ�������µĲ�����ֱ�������µķ�������



//������ģʽ�����ȣ��ö���һ�������߻��࣬������������Ԫ����Ҫ�Ĳ���������˵equip������kill����������Ϊ�˷���ֻ������2��������
//���е�equipmentElement��equipmentElement�࣬����ʵ������Ԫ�أ�������Ҫ�ľ���ʵ�֡�
