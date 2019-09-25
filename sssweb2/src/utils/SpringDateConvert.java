package utils;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpringDateConvert extends PropertyEditorSupport{

	public void setAsText(String text) {
		String value = text.trim();
		Date date = null;
		try {
			if (text==null || "".equals(value)) {
				date = null;
			}else if(text.matches("^\\d{4}-\\d{2}-\\d{2}$")){
				date = new SimpleDateFormat("yyyy-MM-dd").parse(text);
			}else if(text.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")){
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
			}else if(text.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d$")){
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(text);
			}else if(text.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}$")){
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(text);
			}
		} catch (Exception e) {
			throw new RuntimeException("转换日期失败,日期:"+text);
		}
		
		if(value!=null && value.length()>0 && date==null){
			throw new RuntimeException("没有对应的转换规则,日期:"+text);
		}
		
		setValue(date);
	}

}