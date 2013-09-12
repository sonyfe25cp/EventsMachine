package gossip.test;

import gossip.utils.DateTimeUtil;

import java.util.Calendar;
import java.util.Date;


public class TestFormatDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(transTodayToInt());
	}
	private  static int transTodayToInt(){
		Date date = Calendar.getInstance().getTime();
		String time = DateTimeUtil.getFormatDay(date);
		return Integer.parseInt(time);
	}
}
