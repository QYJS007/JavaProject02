package utils.other;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	
	/**
	 * 获得全拼
	 * */
	public static String getPingYin(String src) throws Exception{
		char[] t1 = null;
		if(src!=null){
			t1=src.toCharArray();
			String[] t2 = new String[t1.length];
			HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
			t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			t3.setVCharType(HanyuPinyinVCharType.WITH_V);
			String t4="";
			int t0=t1.length;
			try {
			  for (int i=0;i<t0;i++){
				  //判断是否为汉字字符
				  if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")){
					 	if(t1[i]=='长'){
					 		t2[0]="chang";
					 	}else if(t1[i]=='那'){
					 		t2[0]="na";
					 	}else if(t1[i]=='坻'){
					 		t2[0]="di";
					 	}else if(t1[i]=='袥'){
					 		t2[0]="tuo";
					 	}else{
					 		t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3); 
					 	}
					 	if(t2[0] != null){
					 		t4+=t2[0];
					 	}
			  	}
				  else
					  t4+=java.lang.Character.toString(t1[i]);
		  	}
			  return t4;
			}catch (BadHanyuPinyinOutputFormatCombination e1) {
			  e1.printStackTrace();
			}
			return t4;
		}else{
			return src;
		}
	}
	
	/**
	 * 获得简拼
	 * */
	public static String getPinYinHeadChar(String str) throws Exception{
		String convert = "";
		if(str!=null){
			for (int j = 0; j < str.length(); j++) {
				  char word = str.charAt(j);
				  String[] pinyinArray=new String[1];
				  if(word=='长'){
					  pinyinArray[0]=new String("c");
			  	}else if(word=='那'){
					  pinyinArray[0]=new String("n");
			  	}else if(word=='坻'){
					  pinyinArray[0]=new String("d");
			  	}else if(word=='袥'){
					  pinyinArray[0]=new String("t");
				 	}else{
					  pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			  	}
				  if (pinyinArray != null) {
					  convert += pinyinArray[0].charAt(0);
			  	}else {
					  convert += word;
			  	}
			}
			return convert;
		}else{
			return str;
		}
	}
	
	/**
	 * 将字符串转移为ASCII码
	 * */
	public static String getCnASCII(String cnStr) throws Exception{
	StringBuffer strBuf = new StringBuffer();
	byte[] bGBK = cnStr.getBytes();
			for(int i=0;i<bGBK.length;i++){
				strBuf.append(Integer.toHexString(bGBK[i]&0xff));
			}
			return strBuf.toString();
	}

}
