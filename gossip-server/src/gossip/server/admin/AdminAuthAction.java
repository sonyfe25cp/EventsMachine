package gossip.server.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin")
@Controller
public class AdminAuthAction {

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam String email, @RequestParam String password){
		boolean flag = false;
		if(email.equals("admin") && password.equals("123123")){
			flag = true;
		}
		if(flag)
			return new ModelAndView("/admin/auth/welcome");
		else
			return new ModelAndView("/admin/auth/login");
	}
	
}
