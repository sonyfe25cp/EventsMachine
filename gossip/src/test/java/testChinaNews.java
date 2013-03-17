//import java.util.Calendar;
//import java.util.List;
//
//import junit.framework.TestCase;
//
//import org.junit.Test;
//
//import edu.bit.dlde.urlTemp.ChinaNews;
//import edu.bit.dlde.utils.DLDELogger;
//
//
//public class testChinaNews extends TestCase{
//
//	private DLDELogger logger=new DLDELogger();
//	
//	@Test
//	public void testReg(){
//		ChinaNews cn=new ChinaNews();
//		String url="http://www.chinanews.com/gn/2012/07-10/4023004.shtml";
//		boolean flag=cn.verify(url);
//		assertEquals(true,flag);
//		logger.info("flag:"+flag+" , url:"+url);
//		
//		String url_wrong="http://www.chinanews.com/gn/2012aa/07-10/4023004.shtml";
//		boolean flag_wrong=cn.verify(url_wrong);
//		assertEquals(false,flag_wrong);
//		logger.info("flag_wrong:"+flag_wrong+" , url:"+url_wrong);
//	}
//	@Test
//	public void testConductor(){
//		ChinaNews cn=new ChinaNews();
//		List<String> urls=cn.baseUrl();
//		for(String url:urls){
//			logger.info(url);
//		}
//	}
//	@Test
//	public void testGetTime(){
//		Calendar cal = Calendar.getInstance();
//		int current_year = cal.get(Calendar.YEAR);
//		int current_month = cal.get(Calendar.MONTH )+1;
//		int current_day=cal.get(Calendar.DATE);
//		
//		logger.info(current_year+" "+current_month+" "+current_day);
//	}
//}
