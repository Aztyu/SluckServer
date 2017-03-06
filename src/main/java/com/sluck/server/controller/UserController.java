package com.sluck.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sluck.server.dao.interfaces.IUserDAO;
import com.sluck.server.entity.User;

@Controller
public class UserController {
	@Autowired
	private IUserDAO user_dao;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody void test(Model model){
		User user = new User();
		user.setName("Test user");
		user.setPassword("Test password");
		
		try{
			user_dao.save(user);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody void register(Model model, @ModelAttribute User user){
		try{
			user_dao.save(user);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody void login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user){
		try{
			user_dao.getUser(user);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
