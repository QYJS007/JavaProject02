package cg.str;

public class StrSplit {
	private void mian() {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		String vttypesetareaindex ="1_0_1;1_1_1;1_2_1;2_0_1;2_1_1;2_2_1;3_0_1;3_1_1;3_2_1;4_0_1;4_1_1;4_2_1;5_0_1;5_1_1;5_2_1;6_0_1;6_1_1;6_2_1;";
		String[] areaindex = vttypesetareaindex.split(";");
		for (int i = 0; i < areaindex.length; i++) {
		//	SeatInfoVO seatInfoVO = new SeatInfoVO();
			if(areaindex[i]!=null){
				String[] seatindex = areaindex[i].split("_");
				//×ùÎ»×ø±ê
				String seatindex1 =seatindex[0]+"_"+seatindex[1];
				
				System.out.println(Integer.valueOf(seatindex[2])+"====="+seatindex1);
			}
		}
		String orgcode ="sdfsds";
		String vttypeid ="ewre";
		String schedulecode="dfsadf";
		String routecode="sddfdgsg";
		String endstationcode="sdfgjj465";
		String key = "querySeatGrade_"+orgcode +vttypeid +schedulecode+routecode+schedulecode+endstationcode;
		
		System.out.println(key);
		System.out.println(key.getBytes());
	}

}
