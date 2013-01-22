package gossip.server.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件
 * @author chenyw
 *
 */
@Controller

public class FileUploadController {
	
	private String eventId="";
//	@RequestMapping(value="/eventManage",method=RequestMethod.GET)
//	public ModelAndView toEventManage() {
//		ModelAndView mav=new ModelAndView("eventManage");
//		mav.addObject("eventId", eventId);
//		return mav;
//	}


	/**
	 * 单文件上传
	 * @param name @RequestParam 取得name字段的值
	 * @param file 文件
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("eventId")
	String Id, @RequestParam("file")
	MultipartFile file) throws IOException {
		String status="success";
		if (!file.isEmpty()) {
			 this.copyFile(file.getInputStream(), file.getOriginalFilename());	
		} 
		else
			status="failed";
		eventId=Id;
		return "redirect:/eventManage?eventId="+eventId+"&status="+status;
	}

	
	  /**
	   * 写文件到本地
	   * @param in
	   * @param fileName
	   * @throws IOException
	   */
	  private void copyFile(InputStream in,String fileName) throws IOException{
		  FileOutputStream fs = new FileOutputStream("d:/upload/"
					+ fileName);
			byte[] buffer = new byte[1024 * 1024];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
			fs.close();
			in.close();
	  }
	 
	
}