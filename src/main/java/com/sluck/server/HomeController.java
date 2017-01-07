package com.sluck.server;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.sluck.server.entity.Message;
import com.sluck.server.entity.Response;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static List<Message> messages = new ArrayList<>();
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/message/list/{id}", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody Response listMessage(Model model, @PathVariable Integer id){
		Response response = new Response();
		if(id != null && messages.size() > id){	//Si un id est passé est qu'il est dans la plage d'index valide
			response.setMessages(messages.subList(id, messages.size()));	//On renvoit seulement les messages du serveur après un certain id
			return response;
		}else{
			return null;
		}
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public @ResponseBody void postMessage(Model model, @ModelAttribute("message") Message message){	//@ModelAttribute permet de récupérer les données directement dans un objet message
		if(messages.isEmpty()){		//À chaque message on définit nous même l'id, la taille de la liste + 1
			message.setId(1);
		}else{
			message.setId(messages.size()+1);
		}
		messages.add(message);
	}
}
