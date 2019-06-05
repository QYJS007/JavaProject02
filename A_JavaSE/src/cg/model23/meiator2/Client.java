package cg.model23.meiator2;


public class Client {

	public static void main(String[] args) {
		//������ͣ�ߡ�������
		MainBoard mediator = new MainBoard();
		//����ͬ����
		CDDriver cd = new CDDriver(mediator);
		CPU cpu = new CPU(mediator);
		VideoCard vc = new VideoCard(mediator);
		SoundCard sc = new SoundCard(mediator);
		
		
		//�õ�ͣ��֪������ͬ��
		mediator.setCdDriver(cd);
		mediator.setCpu(cpu);
		mediator.setVideoCard(vc);
		mediator.setSoundCard(sc);
		//��ʼ����Ӱ���ѹ��̷��������������ʼ����
		cd.readCD();

	}

}