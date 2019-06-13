/*package model23.builder;

public class A_Test {

} 



//���ģʽ֮Builder

	
	Builderģʽ����:
	
		
��һ�����Ӷ���Ĺ��������ı�ʾ����,ʹ��ͬ���Ĺ������̿��Դ�����ͬ�ı�ʾ.

Builderģʽ��һ��һ������һ�����ӵĶ���,�������û�����ֻͨ��ָ�����Ӷ�������ͺ����ݾͿ��Թ�������.

	�û���֪���ڲ��ľ��幹��ϸ��.Builderģʽ�Ƿǳ����Ƴ��󹤳�ģʽ,ϸ΢��������ֻ���ڷ���ʹ���в�����ᵽ.

Ϊ��ʹ��?
		��Ϊ�˽��������Ӷ���Ĺ��̺����Ĳ�������.

	ע��: �ǽ�����̺Ͳ���.

	��Ϊһ�����ӵĶ���,�����кܶ������ɲ���,������,�кܶಿ��:���� ������ ���������и���С����ȵ�,
	
	�����ܶ�,��Զ��ֹ��Щ,��ν���Щ����װ���һ������,���װ�����Ҳ�ܸ���(��Ҫ�ܺõ���װ����),Builderģʽ����Ϊ�˽���������װ���̷ֿ�.

���ʹ��?
���ȼ���һ�����Ӷ������ɶ��������ɵ�,Builderģʽ�ǰѸ��Ӷ���Ĵ����Ͳ����Ĵ����ֱ���,�ֱ���Builder���Director������ʾ.

����,��Ҫһ���ӿ�,��������δ������Ӷ���ĸ�������:

public interface Builder { 

��//��������A�������紴����������
��void buildPartA(); 
��//��������B ���紴������������
��void buildPartB(); 
��//��������C ���紴������������
��void buildPartC(); 

��//���������װ��Ʒ��� (�������װ��õ�����)
��//��Ʒ����װ���̲����������,����ת�Ƶ������Director���н���.
��//�Ӷ�ʵ���˽�����̺Ͳ���
��Product getResult(); 

} 


��Director�������ĸ��Ӷ���,��������Builder�ӿ��з�װ������δ���һ��������(���Ӷ���������Щ������ɵ�),

Ҳ����˵Director����������ν����������װ�ɳ�Ʒ:

public class Director {

��private Builder builder; 

��public Director( Builder builder ) { 
������this.builder = builder; 
��} 

��// ������partA partB partC �����ɸ��Ӷ���
��//�����ǽ����� �����̺ͷ�������װ�������Ĺ���
��public void construct() { 
������builder.buildPartA();
������builder.buildPartB();
������builder.buildPartC(); 

��} 

} 


Builder�ľ���ʵ��ConcreteBuilder:
ͨ��������ɽӿ�Builder��������װ���Ʒ�Ĳ���;
���岢��ȷ����Ҫ��������ʲô���嶫��;
�ṩһ���������»�ȡ��Ʒ�Ľӿ�:

public class ConcreteBuilder implements Builder { 

��Part partA, partB, partC; 
��public void buildPartA() {
������//�����Ǿ�����ι���partA�Ĵ���

��}; 
��public void buildPartB() { 
������//�����Ǿ�����ι���partB�Ĵ���
��}; 
�� public void buildPartC() { 
������//�����Ǿ�����ι���partB�Ĵ���
��}; 
�� public Product getResult() { 
������//���������װ��Ʒ���
��}; 

}


���Ӷ���:��ƷProduct:

public interface Product { } 


���Ӷ���Ĳ���:

public interface Part { }



���ǿ�����ε���Builderģʽ:
ConcreteBuilder builder = new ConcreteBuilder();
Director director = new Director( builder ); 

director.construct(); 
Product product = builder.getResult(); 

Builderģʽ��Ӧ��
��Javaʵ��ʹ����,���Ǿ����õ�"��"(Pool)�ĸ���,����Դ�ṩ���޷��ṩ�㹻����Դ,������Щ��Դ��Ҫ���ܶ��û���������ʱ,����Ҫʹ�ó�.

"��"ʵ����һ���ڴ�,��������һЩ���ӵ���Դ��"��֫"(�������ݿ�����ӳ�,Ҳ����ʱһ�����ӻ��ж�),���ѭ����������Щ"��֫",
	
	������ڴ�ʹ��Ч��,��߳ص�����.�޸�Builderģʽ��Director��ʹ֮�����"��֫"�����ĸ�������,���޸��������.

����Ӣ�����¼�:Recycle broken objects in resource pools






���ģʽ֮Proxy(����)
��������banq http://www.jdon.com 2002/04/21/

��Ⲣʹ�����ģʽ,�ܹ������������õ����������ϰ��,ͬʱ��ʵ��Ӧ����,���������ˮ,���������������Ȥ.

Proxy�ǱȽ�����;��һ��ģʽ,���ұ��ֽ϶�,Ӧ�ó��ϸ��Ǵ�С�ṹ������ϵͳ�Ĵ�ṹ,
	Proxy�Ǵ������˼,����Ҳ���д���������ȸ���,���������Խ���Ϊ:�ڳ����㵽Ŀ�ĵ�֮����һ���м��,��Ϊ����.

���ģʽ�ж���: Ϊ���������ṩһ�ִ����Կ��ƶ��������ķ���.

ΪʲôҪʹ��Proxy?
	1.��Ȩ���� ��ͬ������û���ͬһ����ӵ�в�ͬ�ķ���Ȩ��,��Jive��̳ϵͳ��,��ʹ��Proxy������Ȩ���ƿ���,������̳��������:ע���û����ο�(δע���û�),Jive�о�ͨ������ForumProxy�����Ĵ����������������û�����̳�ķ���Ȩ��.

	2.ĳ���ͻ��˲���ֱ�Ӳ�����ĳ������,���ֱ�����Ǹ�������������.
	
���������������: 
(1)����Ǹ�������һ���Ǻܴ��ͼƬ,��Ҫ���Ѻܳ�ʱ�������ʾ����,��ô�����ͼƬ�������ĵ���ʱ,ʹ�ñ༭���������������ĵ�,���ĵ������Ѹ��,���ܵȴ���ͼƬ�������,��ʱ��Ҫ����ͼƬProxy������������ͼƬ.

(2)����Ǹ�������Internet��ĳ��Զ�˷�������,ֱ�Ӳ������������Ϊ�����ٶ�ԭ����ܱȽ���,�����ǿ�������Proxy�������Ǹ�����.

��֮ԭ����,���ڿ����ܴ�Ķ���,ֻ����ʹ����ʱ�Ŵ���,���ԭ�����Ϊ���ǽ�ʡ�ܶ౦���Java�ڴ�. ����,��Щ����ΪJava�ķ���Դ�ڴ�,����Ϊ��ͳ������˼·Ҳ��һ���Ĺ�ϵ.

���ʹ��Proxy?
��Jive��̳ϵͳΪ��,������̳ϵͳ���û��ж�������:ע����ͨ�û� ��̳������ ϵͳ������ �ο�,ע����ͨ�û����ܷ���;��̳�����߿��Թ���������Ȩ����̳;ϵͳ�����߿��Թ������������,��ЩȨ�޻��ֺ͹�����ʹ��Proxy��ɵ�.

Forum��Jive�ĺ��Ľӿ�,��Forum�г������й���̳��������Ҫ��Ϊ,����̳���� ��̳�����Ļ�ȡ���޸�,���ӷ���ɾ���༭��.

��ForumPermissions�ж����˸��ּ���Ȩ�޵��û�:

public class ForumPermissions implements Cacheable { 

/**
* Permission to read object.

public static final int READ = 0;

*//**
* Permission to administer the entire sytem.
*//*
public static final int SYSTEM_ADMIN = 1;

*//**
* Permission to administer a particular forum.
*//*
public static final int FORUM_ADMIN = 2;

*//**
* Permission to administer a particular user.
*//*
public static final int USER_ADMIN = 3;

*//**
* Permission to administer a particular group.
*//*
public static final int GROUP_ADMIN = 4;

*//**
* Permission to moderate threads.
*//*
public static final int MODERATE_THREADS = 5;

*//**
* Permission to create a new thread.
*//*
public static final int CREATE_THREAD = 6;

*//**
* Permission to create a new message.
*//*
public static final int CREATE_MESSAGE = 7;

*//**
* Permission to moderate messages.
*//*
public static final int MODERATE_MESSAGES = 8;

.....

public boolean isSystemOrForumAdmin() {
����return (values[FORUM_ADMIN] || values[SYSTEM_ADMIN]);
}

.....

}
 

// ���,Forum�и��ֲ���Ȩ���Ǻ�ForumPermissions������û������й�ϵ��,��Ϊ�ӿ�Forum��ʵ��:ForumProxy���ǽ����ֶ�Ӧ��ϵ��ϵ����.����,�޸�Forum������,ֻ����̳�����߻�ϵͳ�����߿����޸�,��������:

public class ForumProxy implements Forum {

private ForumPermissions permissions;
private Forum forum; 
this.authorization = authorization; 

public ForumProxy(Forum forum, Authorization authorization,
ForumPermissions permissions)
{
this.forum = forum;
this.authorization = authorization;
this.permissions = permissions;
}

.....

public void setName(String name) throws UnauthorizedException,
ForumAlreadyExistsException
{
����//ֻ����ϵͳ����̳�����߲ſ����޸�����
����if (permissions.isSystemOrForumAdmin()) {
��������forum.setName(name);
����}
����else {
��������throw new UnauthorizedException();
����}
}

...

}
 

// ��DbForum���ǽӿ�Forum������ʵ��,���޸���̳����Ϊ��:

public class DbForum implements Forum, Cacheable {
...

public void setName(String name) throws ForumAlreadyExistsException {

����....

����this.name = name;
����//���������������Ʊ��浽���ݿ��� 
����saveToDb();

����....
}


... 

}
 

�����漰������̳�����޸���һ�¼�,�����������ȵú�ForumProxy�򽻵�,��ForumProxy�����Ƿ���Ȩ����ĳһ������,ForumProxy�Ǹ�������ʵ��"����","��ȫ����ϵͳ".

��ƽʱӦ����,�޿ɱ�����Ҫ�漰��ϵͳ����Ȩ��ȫ��ϵ,������������ʶ��ʹ��Proxy,ʵ�����Ѿ���ʹ��Proxy��.

���Ǽ������Jive̸����һ��,����Ҫ�漰������ģʽ��,����㲻�˽⹤��ģʽ,�뿴�ҵ�����һƪ����:���ģʽ֮Factory

�����Ѿ�֪��,ʹ��Forum��Ҫͨ��ForumProxy,Jive�д���һ��Forum��ʹ��Factoryģʽ,��һ���ܵĳ�����ForumFactory,�������������,
����ForumFactory��ͨ��getInstance()����ʵ��,����ʹ����Singleton(Ҳ�����ģʽ֮һ,���ڽ������ºܶ�,�ҾͲ�д��,������),getInstance()���ص���ForumFactoryProxy.

Ϊʲô������ForumFactory,������ForumFactory��ʵ��ForumFactoryProxy?
ԭ�������Ե�,��Ҫͨ������ȷ���Ƿ���Ȩ�޴���forum.

��ForumFactoryProxy�����ǿ�����������:

public class ForumFactoryProxy extends ForumFactory { 

����protected ForumFactory factory;
����protected Authorization authorization;
����protected ForumPermissions permissions;

����public ForumFactoryProxy(Authorization authorization, ForumFactory factory,
����ForumPermissions permissions)
����{
��������this.factory = factory;
��������this.authorization = authorization;
��������this.permissions = permissions;
����}

����public Forum createForum(String name, String description)
������������throws UnauthorizedException, ForumAlreadyExistsException
����{
��������//ֻ��ϵͳ�����߲ſ��Դ���forum 
��������if (permissions.get(ForumPermissions.SYSTEM_ADMIN)) {
������������Forum newForum = factory.createForum(name, description);
������������return new ForumProxy(newForum, authorization, permissions);
��������}
��������else {
������������throw new UnauthorizedException();
����}
}
 

����createForum���ص�Ҳ��ForumProxy, Proxy����һ��ǽ,��������ֻ�ܺ�Proxy��������.

ע�⵽����������Proxy:ForumProxy��ForumFactoryProxy. ����������ͬ��ְ��:ʹ��Forum�ʹ���Forum;
����Ϊʲô��ʹ�ö���ʹ�������ֿ�,��Ҳ��Ϊʲôʹ��Factoryģʽ��ԭ������:��Ϊ��"��װ" "����";���仰˵,�����ܹ��ܵ�һ��,����ά���޸�.

Jive��̳ϵͳ�����������ӵĴ�����ʹ��,���ǰ���Forum���˼·������.

�����������������ʹ��Proxy������Ȩ���Ƶķ���,Proxy�����Զ��û���������һ�ֳ�Ϊcopy-on-write���Ż���ʽ.����һ���Ӵ�����ӵĶ�����һ�������ܴ�Ĳ���,�������������,û�ж�ԭ���Ķ��������޸�,��ô�����Ŀ���������û�б�Ҫ.�ô����ӳ���һ��������.

����:������һ���ܴ��Collection,������hashtable,�кܶ�ͻ��˻Ტ��ͬʱ������.����һ���ر�Ŀͻ���Ҫ�������������ݻ�ȡ,��ʱҪ�������ͻ��˲�������hashtable�����ӻ�ɾ�� ����.

��ֱ�ӵĽ��������:ʹ��collection��lock,�����ر�Ŀͻ��˻�����lock,�������������ݻ�ȡ,Ȼ�����ͷ�lock.
public void foFetches(Hashtable ht){
����synchronized(ht){
��������//������������ݻ�ȡ����.. 
����} 

}

������һ�취������סCollection��ܳ�ʱ��,���ʱ��,�����ͻ��˾Ͳ��ܷ��ʸ�Collection��.

�ڶ������������clone���Collection,Ȼ�������������ݻ�ȡ���clone�������Ǹ�Collection����.�������ǰ����,���Collection�ǿ�clone��,���ұ������ṩ���clone�ķ���.Hashtable���ṩ�˶��Լ���clone����,������Key��value�����clone,����Clone������Բο�ר������.
public void foFetches(Hashtable ht){

����Hashttable newht=(Hashtable)ht.clone();

}

����������,���������clone�����Ķ������,���ԭ����ĸ�屻�����ͻ��˲����޸���, ��ô��clone�����Ķ��������û��������.

���������:���ǿ��Ե������ͻ����޸���ɺ��ٽ���clone,Ҳ����˵,����ر�Ŀͻ�����ͨ������һ����clone�ķ���������һϵ�����ݻ�ȡ����.��ʵ����û�������Ľ��ж��󿽱�,ֱ���������ͻ����޸����������Collection.

ʹ��Proxyʵ���������.�����copy-on-write����.

ProxyӦ�÷�Χ�ܹ�,�������еķֲ����㷽ʽRMI��Corba�ȶ���Proxyģʽ��Ӧ��.

����ProxyӦ��,��http://www.research.umbc.edu/~tarr/cs491/lectures/Proxy.pdf

Sun��˾�� Explore the Dynamic Proxy API Dynamic Proxy Classes













 


 
 
 
 
 
 




*/