package cg.model23.visitor;


/**
 * 电脑的零配件的父抽象类
 * @author wangXgnaw
 *
 */
public abstract class ComputerPart {
    
    /**
     * 所有的 零配件，都必须通过一个硬件接口进行连接
     * @param hardwareInterface
     */
    protected abstract void link(HardwareInterface hardwareInterface);

}

/**
 * 电脑的硬件CPU，用于数据的运算
 * 
 * @author wangXgnaw
 *
 */
 class CPU extends ComputerPart {
    @Override
    protected void link(HardwareInterface hardwareInterface) {
        // 先得通过接口连接数据
        hardwareInterface.visitor(this);
        // 连接完了之后，就开始使用cpu
        System.out.println("连接上了之后，利用cpu进行计算数据");
    }

}

 /**
  * 电脑硬件之显卡 通过显卡可以进行电脑的屏幕图像的显示
  * 
  * @author wangXgnaw
  *
  */
 class VideoCard extends ComputerPart {
     @Override
     protected void link(HardwareInterface hardwareInterface) {
         // 必须先用接口连接上显卡
         hardwareInterface.visitor(this);
         System.out.println("连接上显卡之后，显卡开始工作，提供图像");
     }
 }
 
 
 /**
  * 电脑硬件之硬盘
  * @author wangXgnaw
  *
  */
 class HardDisk extends ComputerPart {
     @Override
     protected void link(HardwareInterface hardwareInterface) {
         // 必须先通过接口，把硬盘先连上，然后才能操作
         hardwareInterface.visitor(this);
         // 硬盘开始干活
         System.out.println("硬盘以及连接上了，开始存储数据");
     }

 }
 
 
 
 /**
  * 电脑的类，当需要装机的话，就先准备好硬件，即new出来，然后插上接口
  * @author wangXgnaw
  */
 class Computer {
     /**
      * 想装机，先得提供硬件接口才行
      * @param hardwareInterface
      */
     public void useComputer(HardwareInterface hardwareInterface){
         //通过接口，连接cpu
         new CPU().link(hardwareInterface);
         //通过接口，连接显卡
         new VideoCard().link(hardwareInterface);
         //通过接口连接硬盘
         new HardDisk().link(hardwareInterface);
         
     }
 }