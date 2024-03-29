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
import com.sluck.server.entity.Message;
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
		
		if(user_db != null && user_db.isEmpty()){
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
	
	@Override
	public List<Conversation> searchConversation(String search) {
		Session session = this.sessionFactory.openSession();

		try{
			Query query = null;
			
			if(search != null && !search.isEmpty()){
				query = session.createQuery("from Conversation c where c.shared = true and c.name like :search");
				query.setParameter("search", "%"+search+"%");
			}else{
				query = session.createQuery("from Conversation c where c.shared = true");
			}
			query.setMaxResults(100);
			
			List<Conversation> conversation_db = (List<Conversation>) query.getResultList();
			
			return conversation_db;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	@Override
	public Conversation hasConversationAccess(User user, int conversation_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation c left join Conversation_User cu on cu.conversation_id = c.id and cu.user_id = :user_id where c.id = :conversation_id and (c.shared = true or cu.user_id is not null)");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("user_id", user.getId());
			
			List<Conversation> conversation_db = (List<Conversation>)query.getResultList();
			
			if(conversation_db != null && !conversation_db.isEmpty()){
				Conversation conversation = conversation_db.get(0);
				addUserToConversation(conversation, user);			//Cette fonction va l'ajouter si il n'est pas déjà ajouté
				return conversation;
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
	public Message sendMessage(Message message) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(message);
		tx.commit();
		session.close();
		
		return message;
	}
	
	@Override
	public List<Message> listMessages(int conversation_id, int message_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("from Message where conversation_id = :conversation_id and id > :message_id");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("message_id", message_id);
			List<Message> messages_db = (List<Message>) query.getResultList();
			
			return messages_db;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
}
