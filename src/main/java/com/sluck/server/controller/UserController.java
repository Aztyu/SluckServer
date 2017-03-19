package com.sluck.server.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluck.server.entity.User;
import com.sluck.server.job.interfaces.IUserJob;

@Controller
public class UserController {
	@Autowired
	private IUserJob user_job;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody void test(Model model){
		User user = new User();
		user.setName("Test user");
		user.setPassword("Test password");
		
		try{
			user_job.save(user);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody void register(Model model, @RequestParam("file") MultipartFile file, @RequestParam("user") String user_json){
		try{
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(user_json, User.class);
			
			user_job.saveThumbnail(file, 666/*new_user.getId()*/);
			
			User new_user = user_job.save(user);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody User login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user){
		try{
			User user_logged = user_job.getUser(user);
			return user_logged;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/user/profile/{user_id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getUserProfile(HttpServletRequest request, HttpServletResponse response, @PathVariable int user_id) throws FileNotFoundException, IOException{
		response.addHeader("Cache-Control", "max-age=600, public");	
		return user_job.getProfile(user_id);
	}
}
