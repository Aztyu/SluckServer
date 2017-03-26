package com.sluck.server.security;

import java.util.HashMap;
import java.util.Map;

import com.sluck.server.entity.User;

public class KeyStore {
	private static Map<String, User> tokens = new HashMap<>();
	
	public static void storeToken(String token, User user){
		tokens.put(token, user);
	}
	
	public static User getLoggedUser(String token){
		if(tokens.containsKey(token)){
			return tokens.get(token);
		}else{
			return null;
		}
	}

	public static boolean hasToken(String token) {
		return tokens.containsKey(token);
	}

	public static void clearToken(String token) {
		tokens.remove(token);
	}
}
