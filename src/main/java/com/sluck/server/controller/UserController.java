package com.sluck.server.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	
	@Autowired
    ServletContext context;
	
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
	public @ResponseBody ResponseEntity<?> register(HttpServletResponse response, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("user") String user_json){
		User new_user = null;
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(user_json, User.class);
			new_user = user_job.save(user);
			new_user.setPassword(null); //On n' envoie pas la mot de passe via JSON
			
			if(file != null){
				user_job.saveThumbnail(file, new_user.getId());
			}
			
			return new ResponseEntity<>(new_user, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/api/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> profileUpdate(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("user") String user_json){
		User user = user_job.getLoggedUser(request.getHeader("Authorization"));
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			User new_user = mapper.readValue(user_json, User.class);
			
			user_job.updateUser(user, new_user);
			
			if(file != null){
				user_job.saveThumbnail(file, new_user.getId());
			}
			
			return new ResponseEntity<>(new_user, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user){
		try{
			User user_logged = user_job.getUser(user);
			
			if(user_logged != null){
				return new ResponseEntity<>(user_logged, HttpStatus.OK);
			}else{
				return new ResponseEntity<>("Login ou mot de passe incorrect", HttpStatus.UNAUTHORIZED);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> reset(HttpServletRequest request, HttpServletResponse response, @RequestParam String email){
		try{
			if(email != null){
				user_job.createResetCode(email);
				return new ResponseEntity<>(HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("Merci de renseigner un mail", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> resetPassword(HttpServletRequest request, HttpServletResponse response){
		try{
			String code = request.getParameter("code");
			String password = request.getParameter("password");
			
			if(code != null && password != null){
				user_job.resetPassword(code, password);
				return new ResponseEntity<>(HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("Merci de rentrer un code et un mot de passe", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/user/profile/{user_id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getUserProfile(HttpServletRequest request, HttpServletResponse response, @PathVariable int user_id) throws FileNotFoundException, IOException{
		response.addHeader("Cache-Control", "max-age=600, public");	
		try{
			return user_job.getProfile(user_id);
		}catch(IOException ex){
			InputStream in = context.getResourceAsStream("/resources/profile.png");
			byte[] image = IOUtils.toByteArray(in);
			if(in != null){
				in.close();
			}
			return image;
		}
	}
	
	@RequestMapping(value = "/api/logout", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
		String token = request.getHeader("Authorization");
		
		User user = user_job.getLoggedUser(token);

		user_job.disconnect(user, token);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
