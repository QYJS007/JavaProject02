package cg.model23.meiator2;
//ͬ���ࡪ���Կ�

public class VideoCard extends Colleague {
   /**
    * ���캯��
    */
   public VideoCard(Mediator mediator) {
       super(mediator);
   }
   /**
    * ��ʾ��Ƶ����
    */
   public void showData(String data){
       System.out.println("�����ڹۿ����ǣ�" + data);
   }
}