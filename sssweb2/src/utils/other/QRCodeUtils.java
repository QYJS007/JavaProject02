package utils.other;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeUtils {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	/**
	 * 生成条形码到流
	 * @param content
	 * @param width
	 * @param height
	 * @param type
	 * @param file
	 */
	public static void writeBarCodeToStream(String content,int width,int height,String type, OutputStream out){
		try {
			// 187 50
			Map<EncodeHintType,String> encodingMap = new HashMap<EncodeHintType,String>();
			encodingMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.CODE_39, width, height, encodingMap);
			BufferedImage image = toBufferedImage(bitMatrix);
		
			ImageIO.write(image, type, out);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成条形码失败");
		}
	}
	
	/**
	 * 生成条形码到图片
	 * @param content
	 * @param width
	 * @param height
	 * @param file
	 */
	public static void writeBarCodeToFile(String content,int width,int height, File file){
		try {
			//获取后缀
			String type = "jpg";
			String[] arr = file.getName().split("\\.");
			if(arr.length!=1){
				type = arr[arr.length-1];
			}
			OutputStream out = new FileOutputStream(file);
			writeBarCodeToStream(content, width, height, type, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成条形码失败");
		}
	}
	
	/**
	 * 生成条形码到图片
	 * @param content
	 * @param width
	 * @param height
	 * @param file
	 */
	public static void writeBarCodeToFile(String content,int width,int height, String path){
		writeBarCodeToFile(content,width,height,new File(path));
	}
	
	/**
	 * 生成二维码到流
	 * @param content
	 * @param width
	 * @param height
	 * @param type
	 * @param file
	 */
	public static void writeQrCodeToStream(String content,int width,int height,String type, OutputStream out){
		try {
			Map<EncodeHintType,String> encodingMap = new HashMap<EncodeHintType,String>();
			encodingMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, encodingMap);
			BufferedImage image = toBufferedImage(bitMatrix);
		
			ImageIO.write(image, type, out);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成二维码失败");
		}
	}

	/**
	 * 生成二维码到图片
	 * @param content
	 * @param width
	 * @param height
	 * @param file
	 */
	public static void writeQrCodeToFile(String content,int width,int height, File file){
		try {
			//获取后缀
			String type = "jpg";
			String[] arr = file.getName().split("\\.");
			if(arr.length!=1){
				type = arr[arr.length-1];
			}
			OutputStream out = new FileOutputStream(file);
			writeQrCodeToStream(content, width, height, type, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成二维码失败");
		}
	}
	
	/**
	 * 生成二维码到图片
	 * @param content
	 * @param width
	 * @param height
	 * @param file
	 */
	public static void writeQrCodeToFile(String content,int width,int height, String path){
		writeQrCodeToFile(content,width,height,new File(path));
	}
	
	
	/**
	 * 读取二维码的内容,从流读取
	 * @param in
	 * @return
	 */
	public static String readQrCodeToStream(InputStream in){
		try {
			BufferedImage image = ImageIO.read(in);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType,String> hints = new HashMap<DecodeHintType,String>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = new MultiFormatReader().decode(binaryBitmap,hints);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("读取二维码失败");
		}
	}
	
	/**
	 * 读取二维码的内容,从文件读取
	 * @param file
	 * @return
	 */
	public static String readQrCodeToFile(File file){
		try {
			InputStream in = new FileInputStream(file);
			return readQrCodeToStream(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("读取二维码失败");
		}
	}
}
