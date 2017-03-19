package com.sluck.server.job.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
	public User save(User u) {
		u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt())); 
		
		user_dao.save(u);
		
		return u;
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

	public byte[] getThumbnailByte(MultipartFile file) throws IOException {
		InputStream in = new ByteArrayInputStream(file.getBytes());
		BufferedImage image = ImageIO.read(in);
		BufferedImage resizedImage = null;
		byte[] image_bytes;
		
		if(image.getHeight() > 256 || image.getWidth() > 256){
			int height = image.getHeight();
			int width = image.getWidth();
			
			double shrink_ratio = 0.0;
			if(height >= width){
				int new_height = 256;
				shrink_ratio = (double)new_height/height;
				int new_width = (int) (width*shrink_ratio);
				
				resizedImage = new BufferedImage(new_width, new_height, image.getType());
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(image, 0, 0, new_width, new_height, null);
				g.dispose();
			}else{
				int new_width = 256;
				shrink_ratio = (double)new_width/width;
				int new_height = (int) (height*shrink_ratio);
				
				resizedImage = new BufferedImage(new_width, new_height, image.getType());
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(image, 0, 0, new_width, new_height, null);
				g.dispose();
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "png", baos);
			baos.flush();
			return baos.toByteArray();
		}else{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ImageIO.write(image, "png", baos);
			baos.flush();
			return baos.toByteArray();
		}
	}
	
	@Override
	public void saveThumbnail(MultipartFile file, int id) throws IOException {
		byte[] thumbnail = getThumbnailByte(file);
		
		FileUtils.writeByteArrayToFile(new File("./images/profile/" + String.valueOf(id)), thumbnail);
	}
	
	@Override
	public byte[] getProfile(int user_id) throws FileNotFoundException, IOException {
		return IOUtils.toByteArray(new FileInputStream(new File("./images/profile/" + String.valueOf(user_id))));
	}
}
