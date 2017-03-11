package com.sluck.server.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Conversation_User;
import com.sluck.server.entity.User;

public class MessageDAO implements IMessageDAO{
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	@Override
	public void addUserToConversation(Conversation conversation, User user) {
		Session session = this.sessionFactory.openSession();

		Query query = session.createQuery("from Conversation_User cu where cu.user_id = :u_id and cu.conversation_id = :c_id");		//On vérifie que l'utilisateur n'appartient pas déjà à la conversation
		query.setParameter("u_id", user.getId());
		query.setParameter("c_id", conversation.getId());
		
		List<User> user_db = (List<User>) query.getResultList();
		
		if(user_db != null || user_db.isEmpty()){
			Conversation_User conversation_user = new Conversation_User();		//
			conversation_user.setConversation_id(conversation.getId());
			conversation_user.setUser_id(user.getId());
			
			Transaction tx = session.beginTransaction();
			session.persist(conversation_user);
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public Conversation createConversation(Conversation conversation) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(conversation);
		tx.commit();
		session.close();
		
		return conversation;
	}
	
	@Override
	public Conversation getConversation(int conversation_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("from Conversation c where c.id = :id");
			query.setParameter("id", conversation_id);
			List<Conversation> conversation_db = (List<Conversation>) query.getResultList();
			
			if(conversation_db != null && !conversation_db.isEmpty()){
				return conversation_db.get(0);
			}else{
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	@Override
	public List<Conversation> getConversationList(User user) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation c left join Conversation_User cu on c.id = cu.conversation_id where cu.user_id = :id");
			query.setParameter("id", user.getId());
			List<Conversation> conversation_db = (List<Conversation>) query.getResultList();
			
			return conversation_db;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
}
