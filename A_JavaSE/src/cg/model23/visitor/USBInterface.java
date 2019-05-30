package cg.model23.visitor;

/*把上面的框架包封装起来，因为访问者模式要求，结构不能变化，只能变化数据操作上。

 */
/**
 * 自定义的接口，实现了硬件接口的借口类
 * @author wangXgnaw
 *
 */
public class USBInterface implements HardwareInterface {

	@Override
	public void visitor(CPU cpu) {
		System.out.println("usb连接cpu");
	}

	@Override
	public void visitor(VideoCard vCard) {
		System.out.println("用usb连接显卡");
	}

	@Override
	public void visitor(HardDisk hd) {
		System.out.println("用usb连接硬盘");
	}

}
