package com.sluck.server.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IUserDAO;
import com.sluck.server.entity.User;

public class UserDAO implements IUserDAO{
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public void save(User u) {
		Session session = this.sessionFactory.openSession();
		
		Query query = session.createQuery("from User u where u.name = :name");
		query.setParameter("name", u.getName());
		List<User> user_db = (List<User>) query.getResultList();
		
		if(user_db != null || user_db.isEmpty()){
			Transaction tx = session.beginTransaction();
			session.persist(u);
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public User getUser(User u) {
		Session session = this.sessionFactory.openSession();
		
		Query query = session.createQuery("from User u where u.name = :name");
		query.setParameter("name", u.getName());
		List<User> user_db = (List<User>) query.getResultList();
		
		if(user_db != null && !user_db.isEmpty()){
			if(BCrypt.checkpw(u.getPassword(), user_db.get(0).getPassword())){
				user_db.get(0).setPassword(null);//On envoie pas le mot de passe via le json
				return user_db.get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> list() {
		Session session = this.sessionFactory.openSession();
		List<User> user_list = (List<User>)session.createQuery("from User").list();
		session.close();
		return user_list;
	}
	
	@Override
	public User getUserDetail(int id) {
		Session session = this.sessionFactory.openSession();
		
		Query query = session.createQuery("select u.name, u.id from User u where u.id = :id");
		query.setParameter("id", id);
		List<Object[]> user_db = (List<Object[]>) query.getResultList();
		
		if(user_db != null && !user_db.isEmpty()){
			Object[] obj = user_db.get(0);
			
			User user = new User();
			user.setName((String) obj[0]);
			user.setId((int) obj[1]);
			
			return user;
		}else{
			return null;
		}
	}
}
