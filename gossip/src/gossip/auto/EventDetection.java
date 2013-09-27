package gossip.auto;

import java.util.Calendar;

import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.DateTrans;

public class EventDetection {

	public static void main(String[] args) {
		DLDELogger logger = new DLDELogger();
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		logger.info("year:" + year + ",month:" + month + ",day:" + day);
		int date_int = DateTrans.YYMMDDToInt(year, month, day);
		for (int i = 0; i < args.length;) {
			if (args[i].equals("-d")) {
				try {
					date_int = Integer.parseInt(args[i + 1]);
				} catch (Exception e) {
					System.out.println("-d 参数不合法，请输入格式为 YYYYMMDD,如 20121206");
					System.exit(0);
				}
				i += 2;
			} else if (args[i].equals("-h")) {
				System.out
						.println("-d 计算某天的相似度,默認值：今天，例如 20121206；若值为0，则表示全部计算");
				System.exit(0);
			}
		}

	}

}
