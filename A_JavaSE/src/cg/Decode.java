package cg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Decode {
	public static void main(String[] args) {
		String ss = "  浣犲ソ,你好,时光飞逝，转眼间我*墓ぷ魇杂闷谝呀咏采Ｕ馐俏胰松忻肿阏涔�*木哺伊粝铝司识篮�*幕匾洹Ｔ谡舛问奔淅锎蠹腋枇宋易愎�*目砣荨⒅С帧⒐睦桶镏梦页浞指惺�*搅肆�*济羌岫�*男拍睿屯旅腔止�*木瘛Ｔ诙源蠹宜嗳黄鹁�*耐保参矣谢岢晌菊皆惫ざ�*焦馊俸托朔堋�";
		System.err.println();
		Scanner scan = new Scanner(System.in);
        //System.out.println("请输入需要改变编码格式的文件位置");
        //String str = scan.nextLine();
        //File file = new File(str);
       // System.out.println("文件的初始编码");
       // String bm1 = scan.nextLine();
       // System.out.println("文件需要转换成的编码");
       // String bm2 = scan.nextLine();
     //   getAllFiles(file, bm1, bm2);
		
		
        String string = "小二，上怀荼";
        /**
         * 默认GBK编码，默认的编码跟建立项目时设置的编码相同
         */
        byte[] bs = ss.getBytes();
        System.out.println(new String(bs));

        try {
            /**
             * 将字符转换为UTF-8编码
             */
            byte[] bs2 = ss.getBytes("Utf-8");
           // System.out.println(new String(bs2, "GBk"));
            byte[] bs4 = ss.getBytes("GBK");
            System.out.println(new String(bs4, "utf-8"));
          //  byte[] bs5 = ss.getBytes("IOS-8859-1");
          //  System.out.println(new String(bs5, "utf-8"));
            /**
             * 将字符转换为Unicode编码，然后以Unicode编码的方法转换为字符串输出
             */
            byte[] bs3 = ss.getBytes("Unicode");
            System.out.println(new String(bs3, "Unicode"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



	}
	/**
	 *
	 * @param file 要编译的文件
	 * @param bm1 文件的初始编码
	 * @param bm2 文件需要转换成的编码
	 * @throws FileNotFoundException 文件找不到
	 * @throws UnsupportedEncodingException 编码出错
	 * @throws IOException io异常
	 */
	public static void getAllFiles(File file, String bm1, String bm2) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		if (file.isDirectory()) {
			File[] test = file.listFiles();
			for (File test1 : test) {
				//类的名字
				String str = test1.getPath();
				if (str.endsWith("java") & test1.isFile()) {
					String[] s = str.split("\\.");
					String filecope = s[0] + "cope." + s[1];
					System.out.println(filecope);
					File fil = new File(filecope);
					//转格式
					InputStreamReader isr = new InputStreamReader(new FileInputStream(test1), bm1);
					OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream(fil), bm2);
					int re = -1;
					while ((re = isr.read()) != -1) {
						osr.write(re);
					}
					isr.close();
					osr.close();
					InputStreamReader isrr = new InputStreamReader(new FileInputStream(fil), bm2);
					OutputStreamWriter osrw = new OutputStreamWriter(new FileOutputStream(test1), bm2);
					int r = -1;
					while ((r = isrr.read()) != -1) {
						osrw.write(r);
					}
					isrr.close();
					osrw.close();
					boolean d = fil.delete();
					System.out.println(str + "文件转换utf-8成功:" + d);
				}
				getAllFiles(test1, bm1, bm2);
			}
		}
	}

}

