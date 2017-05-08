package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.Reset;
import com.sluck.server.entity.User;

public interface IUserDAO {
	public void save(User u);
	public User getUser(User u);
	public List<User> list();
	public User getUserDetail(int id);
	public User getUserByMail(String email);
	public String createResetCode(int id);
	public Reset getReset(String code);
	public void updateUser(User user);
}
