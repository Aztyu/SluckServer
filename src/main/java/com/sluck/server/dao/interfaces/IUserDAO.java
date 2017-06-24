package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.Reset;
import com.sluck.server.entity.Token;
import com.sluck.server.entity.User;

public interface IUserDAO {
	public void createUser(User u);
	public User getUser(User u);
	public List<User> list();
	public User getUserDetail(int id);
	public User getUserByMail(String email);
	public String createResetCode(int id);
	public Reset getReset(String code);
	public void updateUser(User user);
	public void updateUserStatus(int id, int status_id);
	public User getUser(int user_id);
	public void setLastLogout(User user);
	public void updatePeerID(int user_id, String peerjs_id);
	public void saveToken(String generateToken, int id);
	public Token findTokenUser(String token);
	public void deleteToken(Token tok);
}
