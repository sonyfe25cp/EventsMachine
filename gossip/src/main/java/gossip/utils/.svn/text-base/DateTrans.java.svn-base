package gossip.utils;

public class DateTrans {

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return YYMMDD Dec 5, 2012
	 */
	public static int YYMMDDToInt(int year, int month, int day) {
		try {
			String date = "" + year + (month < 10 ? ("0" + month) : month)
					+ (day < 10 ? ("0" + day) : day);
			return Integer.parseInt(date);
		} catch (NumberFormatException e) {
			System.out.println(year + "=" + month + "=" + day);
			return 0;
		}
	}

	public static String intDateToString(int date) {
		int year = date / (12 * 31);
		int month = (date - (12 * 31 * year)) / 31;
		int day = date - year * 12 * 31 - month * 31;
		return year + "-" + month + "-" + day;
	}

	public static int theDayBeforeYYMMDD(int yymmdd) {
		String ymd = yymmdd + "";
		if(ymd.length()!=8){
			throw new DateException("日期不为8位");
		}
		int year = Integer.parseInt(ymd.substring(0, 4));
		int month = Integer.parseInt(ymd.substring(4, 6));
		int day = Integer.parseInt(ymd.substring(6, 8));
		int newYear = year;
		int newMonth = month;
		int newDay = day - 1;
		if (day == 1) {
			switch (month) {
			case 1:
				newDay = 31;
				if(month == 1){
					newMonth = 12;
				}else{
					newMonth = month - 1;
				}
				newYear = year - 1;
				break;
			case 2:
				newDay = 30;
				break;
			case 3:
				newDay = 29;
				break;
			case 4:
				newDay = 30;
				break;
			case 5:
				newDay = 31;
				break;
			case 6:
				newDay = 30;
				break;
			case 7:
				newDay = 31;
				break;
			case 8:
				newDay = 31;
				break;
			case 9:
				newDay = 30;
				break;
			case 10:
				newDay = 31;
				break;
			case 11:
				newDay = 30;
				break;
			case 12:
				newDay = 31;
				break;
			}
		}
		return YYMMDDToInt(newYear, newMonth, newDay);
	}

	
	public static int theDayAfterYYMMDD(int yymmdd) {
		String ymd = yymmdd + "";
		if(ymd.length()!=8){
			throw new DateException("日期不为8位");
		}
		int year = Integer.parseInt(ymd.substring(0, 4));
		int month = Integer.parseInt(ymd.substring(4, 6));
		int day = Integer.parseInt(ymd.substring(6, 8));
		int newYear = year;
		int newMonth = month;
		int newDay = day + 1;
		
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month ==10 || month == 12 ){
			if(day == 31){
				newDay = 1;
				newMonth = month + 1;
				if(newMonth > 12){
					newMonth = 1;
					newYear = year + 1;
				}
			}
		}else if(month == 2){
			if(day == 28 ){
				newDay = 1;
				newMonth = month+1;
			}
		}else{
			if(day == 30){
				newDay = 1;
				newMonth = month +1;
			}
		}
		return YYMMDDToInt(newYear, newMonth, newDay);
	}
}
