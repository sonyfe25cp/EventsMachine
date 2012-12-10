import gossip.utils.DateTrans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;


public class testParseDate extends TestCase {
	@Test
	public void testparseDateQQ(){
		//2012年06月28日02:51
//		DateTools dt;//存日期
		
		String d="2012年06月28日02:51";
		DateFormat df= new SimpleDateFormat("yyyy年MM月dd日hh:mm");
		try {
			Date date=df.parse(d);
			
			System.out.println(date);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testStrintToInt(){
		String createAt="2012年06月28日02:51";
		int year=Integer.parseInt(createAt.substring(0, 4));
		int month=Integer.parseInt(createAt.substring(5,7));
		int day=Integer.parseInt(createAt.substring(8,10));
		System.out.println(""+year+" "+month+" "+day);
		System.out.println( year*12*31+month*31+day);
	}
	@Test
	public void testParseDateFromInt(){
		
		int[] date = {748700,748708,748501,748600};
		
		for(int d:date){
			System.out.println(DateTrans.intDateToString(d));
		}
	}
	@Test
	public void testTheDayBeforeYYMMDD(){
		
		int[] date = {20121101,20120301,20121212};
		
		for(int d:date){
			System.out.println(DateTrans.theDayBeforeYYMMDD(d));
		}
	}
	@Test
	public void testGetPagesDate(){
		String filename="newsgn_qq_2012-07-13-02-00-05.pages";
		String year = filename.substring(10, 14);
		String month = filename.substring(15, 17);
		String day = filename.substring(18, 20);
		System.out.println(year+"-"+month+"-"+day);
	}
	@Test
	public void  testParseDateFromUrl(){
		String url = "http://news.qq.com/a/20121025/000987.htm";
		String date = url.substring(21,29);
		System.out.println(date);
	}
	
	@Test
	public void testTheDayAfterYYMMDD(){
		
		int[] date = {20121130,20120228,20121212};
		
		for(int d:date){
			System.out.println(DateTrans.theDayAfterYYMMDD(d));
		}
	}
}
