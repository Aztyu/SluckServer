package com.sluck.server.job.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sluck.server.entity.User;

public interface IUserJob {
	public User save(User u);
	public User getUser(User u);
	public List<User> list();
	public void saveThumbnail(MultipartFile file, int id) throws IOException;
	public byte[] getProfile(int user_id) throws FileNotFoundException, IOException;
}
