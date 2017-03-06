package com.sluck.server.job.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IUserDAO;
import com.sluck.server.entity.User;
import com.sluck.server.job.interfaces.IUserJob;
import com.sluck.server.security.KeyStore;

@Component
public class UserJob implements IUserJob{
	private SecureRandom random = new SecureRandom();
	
	@Autowired
	IUserDAO user_dao;
	
	@Override
	public void save(User u) {
		u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt())); 
		
		user_dao.save(u);
	}

	@Override
	public User getUser(User u) {
		User logged_user = user_dao.getUser(u);
		
		logged_user.setToken(generateToken());
		KeyStore.storeToken(logged_user.getToken(), logged_user);
		
		return logged_user;
	}

	private String generateToken() {
		return new BigInteger(130, random).toString(32);
	}

	@Override
	public List<User> list() {
		return user_dao.list();
	}

}
