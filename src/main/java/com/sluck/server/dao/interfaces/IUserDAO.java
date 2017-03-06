package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.User;

public interface IUserDAO {
	public void save(User u);
	public User getUser(User u);
	public List<User> list();
}
