package cg.model23.visitor;


/**
 * ���Ե�������ĸ�������
 * @author wangXgnaw
 *
 */
public abstract class ComputerPart {
    
    /**
     * ���е� �������������ͨ��һ��Ӳ���ӿڽ�������
     * @param hardwareInterface
     */
    protected abstract void link(HardwareInterface hardwareInterface);

}

/**
 * ���Ե�Ӳ��CPU���������ݵ�����
 * 
 * @author wangXgnaw
 *
 */
 class CPU extends ComputerPart {
    @Override
    protected void link(HardwareInterface hardwareInterface) {
        // �ȵ�ͨ���ӿ���������
        hardwareInterface.visitor(this);
        // ��������֮�󣬾Ϳ�ʼʹ��cpu
        System.out.println("��������֮������cpu���м�������");
    }

}

 /**
  * ����Ӳ��֮�Կ� ͨ���Կ����Խ��е��Ե���Ļͼ�����ʾ
  * 
  * @author wangXgnaw
  *
  */
 class VideoCard extends ComputerPart {
     @Override
     protected void link(HardwareInterface hardwareInterface) {
         // �������ýӿ��������Կ�
         hardwareInterface.visitor(this);
         System.out.println("�������Կ�֮���Կ���ʼ�������ṩͼ��");
     }
 }
 
 
 /**
  * ����Ӳ��֮Ӳ��
  * @author wangXgnaw
  *
  */
 class HardDisk extends ComputerPart {
     @Override
     protected void link(HardwareInterface hardwareInterface) {
         // ������ͨ���ӿڣ���Ӳ�������ϣ�Ȼ����ܲ���
         hardwareInterface.visitor(this);
         // Ӳ�̿�ʼ�ɻ�
         System.out.println("Ӳ���Լ��������ˣ���ʼ�洢����");
     }

 }
 
 
 
 /**
  * ���Ե��࣬����Ҫװ���Ļ�������׼����Ӳ������new������Ȼ����Ͻӿ�
  * @author wangXgnaw
  */
 class Computer {
     /**
      * ��װ�����ȵ��ṩӲ���ӿڲ���
      * @param hardwareInterface
      */
     public void useComputer(HardwareInterface hardwareInterface){
         //ͨ���ӿڣ�����cpu
         new CPU().link(hardwareInterface);
         //ͨ���ӿڣ������Կ�
         new VideoCard().link(hardwareInterface);
         //ͨ���ӿ�����Ӳ��
         new HardDisk().link(hardwareInterface);
         
     }
 }