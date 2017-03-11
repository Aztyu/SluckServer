package com.sluck.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;
import com.sluck.server.job.interfaces.IMessageJob;
import com.sluck.server.job.interfaces.IUserJob;
import com.sluck.server.security.KeyStore;

@Controller
public class ApiController {
	@Autowired 
	IUserJob user_job;
	
	@Autowired
	IMessageJob message_job;
	
	@RequestMapping(value = "/api/test", method = RequestMethod.GET)
	public @ResponseBody String postMessage(HttpServletRequest request, Model model){	//@ModelAttribute permet de récupérer les données directement dans un objet message
		System.out.println(KeyStore.getLoggedUser(request.getHeader("Authorization")).getId());
		
		return "It's working";
	}
	
	@RequestMapping(value = "/api/conversation/create", method = RequestMethod.POST)
	public @ResponseBody Conversation createConversation(HttpServletRequest request, @ModelAttribute Conversation conversation){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		conversation = message_job.createConversation(conversation, user);
		
		return conversation;
	}
	
	@RequestMapping(value = "/api/conversation/list", method = RequestMethod.GET)
	public @ResponseBody List<Conversation> listConversation(HttpServletRequest request){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.getConversationList(user);	
	}
	
	@RequestMapping(value = "/api/conversation/join/{conversation_id}", method = RequestMethod.GET)
	public @ResponseBody void joinConversation(HttpServletRequest request, @PathVariable int conversation_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		message_job.joinConversation(conversation_id, user);
	}
	
	@RequestMapping(value = "/api/conversation/search", method = RequestMethod.GET)
	public @ResponseBody List<Conversation> searchConversation(HttpServletRequest request, @RequestParam(required = false) String search){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.searchConversation(search);
	}
	
	@RequestMapping(value = "/api/message/send/{conversation_id}", method = RequestMethod.POST)
	public @ResponseBody List<Conversation> sendMessage(HttpServletRequest request, @PathVariable String conversation_id, @ModelAttribute Message message){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.sendMessage(user, message, conversation_id);
	}
}
