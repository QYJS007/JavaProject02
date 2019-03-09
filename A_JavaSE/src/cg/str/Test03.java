package cg.str;

public class Test03 {

	public static void main(String[] args) {
		String reg = "^[0-9a-zA-Z]+$";
		String expressNo ="12312315654@#@";
		if(!expressNo .matches(reg)){
			//Logger.applogErrorDetail(null, null, "确认开票", "发票确认开票,订单号只能由数字和大写字母组成，快递单号"+expressNo);
			//return new Result(false,"快递单号只能由数字和字母组成");
			System.out.println("不匹配,快递单号只能由数字和字母组成 ");
		}	else{
			System.out.println("匹配 ");

		}

	}
}
