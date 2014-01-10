package gossip.server.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AbstractAction {
	
	@RequestMapping(value = "/abstract")
	public ModelAndView toAbstract(){
		ModelAndView mav = new ModelAndView("/abstract/abstract");
		return mav;
	}

}
