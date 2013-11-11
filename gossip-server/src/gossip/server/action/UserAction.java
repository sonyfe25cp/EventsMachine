
package gossip.server.action;

import gossip.model.User;
import gossip.server.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户相关的action，登录与注册功能
 * 
 * @author liyan
 *
 */
@Controller
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户注册
	 * @param username
	 * @param password
	 * @param email
	 * @return 返回JSON字符串
	 * 注册成功：{"status":"true"}
	 * 注册失败：{"status":"false"}
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject register(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("email") String email) {
		JSONObject json = new JSONObject();
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setRole("user");
		userService.addUser(user);
	    json.put("status", "true");
		return json;
	}
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param request
	 * @param session
	 * @return 返回JSON字符串
	 * 登陆成功：{"status":"true","info":{"name":"","role":"","last_login_time":"","last_login_ip":""}}
	 * 登录失败：{"status":"false","info":"错误信息"}
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject login(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request,
			HttpSession session) {
		JSONObject json = new JSONObject();
		JSONObject info = new JSONObject();
		if(userService.existUser(username, password)) 
		{
			User user = userService.getUser(username);
			info.put("name", user.getUsername());
			info.put("role", user.getRole());
			if(user.getLastLoginTime() != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				info.put("last_login_time", format.format(user.getLastLoginTime()));
			} else {
				info.put("last_login_time", "");
			}
			if(user.getLastLoginIp() != null) {
				info.put("last_login_ip", user.getLastLoginIp());
			} else {
				info.put("last_login_ip", "");
			}
			
			json.put("status", "true");
			json.put("info", info);
			//获得此次登陆的时间及ip
			String ip = request.getRemoteAddr();
			long date = new Date().getTime();
			userService.updateLoginInfo(username, date, ip);
			//将用户名放入session
			session.setAttribute("currentUser", username);
		} else if(!userService.existUser(username)) {
			json.put("status", "false");
			json.put("info", "用户不存在");
		} else {
			json.put("status", "false");
			json.put("info", "密码错误");
		}
		return json;
	}
	
	/**
	 * 用户注销
	 * @param session
	 */
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public void logout(HttpSession session) {
		if(session.getAttribute("currentUser") != null) {
			session.removeAttribute("currentUser");
		}
	}
	
	/**
	 * 验证用户名是否存在
	 * @param username
	 * @return 返回JSON字符串
	 * 用户存在：{"status":"false","info":"用户已存在"}
	 * 用户不存在：{"status":"true"}
	 */
	@RequestMapping(value="/verifyUsername", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject verifyUsername(
			@RequestParam("username") String username) {
		JSONObject json = new JSONObject();
		
		if(userService.existUser(username)) {
			json.put("status", "false");
			json.put("info", "用户已存在");
		} else {
			json.put("status", "true");
		}
		return json;
	}
	
	/**
	 * 验证email是否存在
	 * @param email
	 * @return 返回JSON字符串
	 * email存在：{"status":"false","info":"email已存在"}
	 * email不存在：{"status":"true"}
	 */
	@RequestMapping(value="/verifyEmail", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject verifyEmail(
			@RequestParam("email") String email) {
		JSONObject json = new JSONObject();
		
		if(userService.existEmail(email)) {
			json.put("status", "false");
			json.put("info", "email已存在");
		} else {
			json.put("status", "true");
		}
		return json;
	}

//	/**
//	 * 记录用户的点击日志
//	 * @param username 登录的用户名
//	 * @param time 时间的格式为yyyy-MM-dd hh:mm:ss
//	 * @param query 当前查询条件，可以为空
//	 * @param objectId 点击对象的ID(事件或新闻)
//	 */
//	@RequestMapping(value="/clicklog", method=RequestMethod.POST)
//	@ResponseBody
//	public void writeClicklog(
//			@RequestParam("username") String username,
//			@RequestParam("time") String time,
//			@RequestParam(value="q", required=false) String query,
//			@RequestParam("id") String objectId) {
//		ClickLog clickLog = new ClickLog();
//		clickLog.setUsername(username);
//		clickLog.setFormatedCreateTime(time);
//		clickLog.setObjectId(Integer.parseInt(objectId));
//		clickLog.setQuery(query);
//
//	}
//	
//	/**
//	 * 记录用户的评论
//	 * @param username 登录的用户名
//	 * @param time 时间的格式为yyyy-MM-dd hh:mm:ss
//	 * @param objectId 评论对象的ID(事件或新闻)
//	 * @param content 评论内容
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping(value="/comment", method=RequestMethod.GET)
//	@ResponseBody
//	public JSONObject writeComment(
//			@RequestParam("username") String username,
//			@RequestParam("time") String time,
//			@RequestParam("id") String objectId,
//			@RequestParam("comment") String content,
//			HttpSession session) {
//		JSONObject json = new JSONObject();
//		if((session.getAttribute("currentUser") == null)
//				|| !(session.getAttribute("currentUser").toString()).equals(username.trim())) {
//			json.put("status", "false");
//			json.put("info", "用户未登录");
//			return json;
//		}
//		Comment comment = new Comment();
//		comment.setUsername(username);
//		comment.setFormatedCreateTime(time);
//		comment.setObjectId(Integer.parseInt(objectId));
//		comment.setContent(content);
//
//		if(cd.addComment(comment)) {
//			json.put("status", "true");
//		} else {
//			json.put("status", "false");
//			json.put("info", "添加评论失败");
//		}
//		return json;
//	}
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
