package cg.model23.visitor;

/*������Ŀ�ܰ���װ��������Ϊ������ģʽҪ�󣬽ṹ���ܱ仯��ֻ�ܱ仯���ݲ����ϡ�

 */
/**
 * �Զ���Ľӿڣ�ʵ����Ӳ���ӿڵĽ����
 * @author wangXgnaw
 *
 */
public class USBInterface implements HardwareInterface {

	@Override
	public void visitor(CPU cpu) {
		System.out.println("usb����cpu");
	}

	@Override
	public void visitor(VideoCard vCard) {
		System.out.println("��usb�����Կ�");
	}

	@Override
	public void visitor(HardDisk hd) {
		System.out.println("��usb����Ӳ��");
	}

}
