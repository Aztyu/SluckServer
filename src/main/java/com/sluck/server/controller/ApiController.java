package com.sluck.server.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;
import com.sluck.server.entity.response.Invitation;
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
	
	/* User */
	
	@RequestMapping(value = "/api/user/detail/{id}", method = RequestMethod.GET)
	public @ResponseBody User getUserDetail(HttpServletRequest request, @PathVariable int id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.getUserDetail(id);	
	}
	
	/* Conversation */
	
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
		
		return message_job.searchConversation(search, user.getId());
	}
	
	@RequestMapping(value = "/api/conversation/quit/{conversation_id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> quitConversation(HttpServletRequest request, @PathVariable int conversation_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		try{
			message_job.quitConversation(conversation_id, user);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	/* Messages */
	
	@RequestMapping(value = "/api/message/send/{conversation_id}", method = RequestMethod.POST)
	public @ResponseBody Message sendMessage(HttpServletRequest request, @PathVariable int conversation_id, @ModelAttribute Message message){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.sendMessage(user, message, conversation_id);
	}
	
	@RequestMapping(value = "/api/message/list/{conversation_id}/{message_id}", method = RequestMethod.GET)
	public @ResponseBody List<Message> listMessage(HttpServletRequest request, @PathVariable int conversation_id, @PathVariable int message_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return message_job.listMessages(user, conversation_id, message_id);
	}
	
	/* Contact */
	
	@RequestMapping(value = "/api/contact/add/{contact_id}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addContact(HttpServletRequest request, @PathVariable int contact_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		try{
			message_job.addContact(user, contact_id);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@RequestMapping(value = "/api/contact/invitation/list", method = RequestMethod.GET)
	public @ResponseBody List<Invitation> getInvitationList(HttpServletRequest request){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));

		return message_job.getInvitationList(user);
	}
	
	@RequestMapping(value = "/api/contact/invitation/{invitation_id}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> acceptInvitation(HttpServletRequest request, @PathVariable int invitation_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));

		try{
			message_job.updateInvitation(user, invitation_id, true);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@RequestMapping(value = "/api/contact/invitation/{invitation_id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> refuseInvitation(HttpServletRequest request, @PathVariable int invitation_id){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));

		try{
			message_job.updateInvitation(user, invitation_id, false);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@RequestMapping(value = "/api/contact/list", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> listContact(HttpServletRequest request){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));

		try{
			return new ResponseEntity<>(message_job.listContact(user.getId()), HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@RequestMapping(value = "/api/contact/rename", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> renameContact(HttpServletRequest request, @ModelAttribute User contact){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));

		try{
			message_job.renameContact(user.getId(), contact);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@RequestMapping(value = "/api/contact/search", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> searchUser(HttpServletRequest request, @RequestParam(required = false) String search){
		User user = KeyStore.getLoggedUser(request.getHeader("Authorization"));
		
		return new ResponseEntity<>(message_job.searchContact(user, search), HttpStatus.OK);
	}
}
