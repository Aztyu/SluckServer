package com.sluck.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sluck.server.entity.Message;
import com.sluck.server.job.interfaces.IUserJob;
import com.sluck.server.security.KeyStore;

@Controller
public class ApiController {
	@Autowired 
	IUserJob user_job;
	
	@RequestMapping(value = "/api/test", method = RequestMethod.POST)
	public @ResponseBody String postMessage(HttpServletRequest request, Model model){	//@ModelAttribute permet de récupérer les données directement dans un objet message
		System.out.println(KeyStore.getLoggedUser(request.getHeader("Authorization")).getId());
		
		return "It's working";
	}
}
