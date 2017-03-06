package com.sluck.server.job.interfaces;

import java.util.List;

import com.sluck.server.entity.User;

public interface IUserJob {
	public void save(User u);
	public User getUser(User u);
	public List<User> list();
}
