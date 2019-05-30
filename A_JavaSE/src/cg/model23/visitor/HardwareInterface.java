package cg.model23.visitor;

public interface HardwareInterface {

	public void visitor(VideoCard videoCard);

	public void visitor(CPU cpu);

	public void visitor(HardDisk hardDisk);

}
