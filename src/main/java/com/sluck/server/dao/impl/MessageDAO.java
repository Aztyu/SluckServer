package com.sluck.server.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;
import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Conversation_Invitation;
import com.sluck.server.entity.Conversation_User;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.MessageFile;
import com.sluck.server.entity.User;
import com.sluck.server.entity.response.ContactSearch;
import com.sluck.server.entity.response.ContactStatus;
import com.sluck.server.entity.response.Conversation_Infos;
import com.sluck.server.entity.response.Invitation;

public class MessageDAO implements IMessageDAO{
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	@Override
	public void addUserToConversation(int conversation_id, int user_id, boolean admin, boolean moderator) {
		Session session = this.sessionFactory.openSession();

		Query query = session.createQuery("from Conversation_User cu where cu.user_id = :u_id and cu.conversation_id = :c_id");		//On vérifie que l'utilisateur n'appartient pas déjà à la conversation
		query.setParameter("u_id", user_id);
		query.setParameter("c_id", conversation_id);
		
		List<User> user_db = (List<User>) query.getResultList();
		
		if(user_db != null && user_db.isEmpty()){
			Conversation_User conversation_user = new Conversation_User();		//
			conversation_user.setConversation_id(conversation_id);
			conversation_user.setUser_id(user_id);
			conversation_user.setAdmin(admin);
			conversation_user.setModerator(moderator);
			
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
	public void saveConversationInvitation(Conversation_Invitation conv_inv) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(conv_inv);
		tx.commit();
		session.close();
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
	public Conversation_User getConversationUser(int conversation_id, int id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("from Conversation_User c_u where c_u.conversation_id = :conversation_id and c_u.user_id = :user_id");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("user_id", id);
			Conversation_User conversation_db = (Conversation_User) query.getSingleResult();
			
			if(conversation_db != null){
				return conversation_db;
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
	public List<Conversation_Infos> getConversationList(User user) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c, (cu.admin or cu.moderator) as admin from Conversation c left join Conversation_User cu on c.id = cu.conversation_id where cu.user_id = :id and c.chat = false");
			query.setParameter("id", user.getId());
			List<Object[]> conv_db = (List<Object[]>) query.getResultList();
			
			if(conv_db != null && !conv_db.isEmpty()){
				List<Conversation_Infos> conversations = new ArrayList<>();
				
				for(Object[] conv_obj : conv_db){
					Conversation_Infos c_i = new Conversation_Infos((Conversation) conv_obj[0], (boolean) conv_obj[1]);
					
					conversations.add(c_i);
				}
				return conversations;
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
	public List<Conversation_Invitation> getConversationInvitationList(User user) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation_Invitation c where c.user_id = :id and c.accepted = false");
			query.setParameter("id", user.getId());
			List<Conversation_Invitation> conversation_db = (List<Conversation_Invitation>) query.getResultList();
			
			return conversation_db;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	@Override
	public List<Conversation> searchConversation(String search, int user_id) {
		Session session = this.sessionFactory.openSession();

		try{
			Query query = null;
			
			if(search != null && !search.isEmpty()){
				query = session.createQuery("select c from Conversation c left join Conversation_User c_u on c.id = c_u.conversation_id and c_u.user_id = :id where shared = true and c_u.id is null and c.chat = false and c.name like :search");
				query.setParameter("search", "%"+search+"%");
				query.setParameter("id", user_id);
			}else{
				query = session.createQuery("select c from Conversation c left join Conversation_User c_u on c.id = c_u.conversation_id and c_u.user_id = :id where shared = true and c_u.id is null and c.chat = false");
				query.setParameter("id", user_id);
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
	public Conversation_Invitation getConversationInvitation(int invitation_id) {
		Session session = null;
		try{ 
			session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			return session.find(Conversation_Invitation.class, invitation_id);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	@Override
	public void quitConversation(Conversation_User conversation_user) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.delete(conversation_user);
		tx.commit();
		session.close();
	}
	
	@Override
	public Conversation hasConversationAccess(User user, int conversation_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation c left join Conversation_User cu on cu.conversation_id = c.id and cu.user_id = :user_id where c.id = :conversation_id and c.chat = false");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("user_id", user.getId());
			
			List<Conversation> conversation_db = (List<Conversation>)query.getResultList();
			
			if(conversation_db != null && !conversation_db.isEmpty()){
				Conversation conversation = conversation_db.get(0);
				addUserToConversation(conversation.getId(), user.getId(), false, false);			//Cette fonction va l'ajouter si il n'est pas déjà ajouté
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
	public boolean hasConversationAdmin(int id, int conversation_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation c left join Conversation_User cu on cu.conversation_id = c.id and cu.user_id = :user_id where c.id = :conversation_id and cu.admin = true or cu.moderator = true");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("user_id", id);
			
			List<Conversation> conversation_db = (List<Conversation>)query.getResultList();
			
			if(conversation_db != null && !conversation_db.isEmpty()){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
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
	public void saveMessageFile(MessageFile file_obj) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(file_obj);
		tx.commit();
		session.close();
	}
	
	@Override
	public MessageFile getMessageFile(int id) {
		Session session = null;
		try{
			session = this.sessionFactory.openSession();
		
			Transaction tx = session.beginTransaction();
			return session.find(MessageFile.class, id);
		}catch(Exception ex){
			return null;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	@Override
	public List<Message> listMessages(int conversation_id, int message_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select m.id, m.content, m.conversation_id, m.user_id, m.time, mj.id, mj.contentType, mj.name from Message m left join MessageFile mj on m.file_id = mj.id where m.conversation_id = :conversation_id and m.id > :message_id order by m.id");
			query.setParameter("conversation_id", conversation_id);
			query.setParameter("message_id", message_id);
			List<Object[]> message_db = (List<Object[]>) query.getResultList();
			
			if(message_db != null && !message_db.isEmpty()){
				List<Message> messages = new ArrayList<>();
				
				for(Object[] message_obj : message_db){
					Message message = new Message();
					
					message.setId((int) message_obj[0]);
					message.setContent((String) message_obj[1]);
					message.setConversation_id((int) message_obj[2]);
					message.setUser_id((int) message_obj[3]);
					message.setTime((Timestamp) message_obj[4]);

					message.setFile_id((message_obj[5] != null) ? (int)(message_obj[5]) : 0);
					
					if(message.getFile_id() > 0){
						MessageFile message_file = new MessageFile();
						message_file.setId(message.getFile_id());
						message_file.setContentType((String) message_obj[6]);
						message_file.setName((String) message_obj[7]);
						
						message.setFile_obj(message_file);
					}
					
					messages.add(message);
				}
				return messages;
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
	public Conversation findChatConversation(int user_id, int contact_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Conversation c left join Conversation_User cu on cu.conversation_id = c.id left join Conversation_User cu1 on cu1.conversation_id = c.id where cu.user_id = :user_id and cu1.user_id = :contact_id and c.chat = true");
			query.setParameter("contact_id", contact_id);
			query.setParameter("user_id", user_id);
			
			List<Conversation> conversation_db = (List<Conversation>)query.getResultList();
			
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
	
	private Contact getContact(int user_id, int contact_id){
		Session session = null;
		
		try{
			session = this.sessionFactory.openSession();
			 
			Query query = session.createQuery("from Contact c where c.contact_id = :contact_id and c.user_id = :user_id");
			query.setParameter("contact_id", contact_id);
			query.setParameter("user_id", user_id);
			List<Contact> contact_db = (List<Contact>) query.getResultList();
			
			if(contact_db != null && !contact_db.isEmpty()){
				return contact_db.get(0);
			}else{
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	@Override
	public boolean isContact(int id, int contact_id) {
		Session session = this.sessionFactory.openSession();
		
		Contact contact = getContact(id, contact_id);
		
		return contact != null;
	}
	
	@Override
	public Contact createContactRequest(int id, User user_contact) {
		Contact contact = new Contact();
		
		contact.setAccepted(false);
		contact.setBlocked(false);
		contact.setName(user_contact.getName());
		contact.setContact_id(user_contact.getId());
		contact.setUser_id(id);
		
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(contact);
		tx.commit();
		session.close();
		
		return contact;
	}
	
	@Override
	public void removeContactRequest(int id, User contact_user) {
		Contact contact = getContact(id, contact_user.getId());
		
		try(Session session = this.sessionFactory.openSession()){
			Transaction tx = session.beginTransaction();
			session.remove(contact);
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public void removeContactInvitation(int invitation_id) {
		Conversation_Invitation conv_invit = getConversationInvitation(invitation_id);
		
		try(Session session = this.sessionFactory.openSession()){
			Transaction tx = session.beginTransaction();
			session.remove(conv_invit);
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public void removeConversationUser(Conversation_User c_u) {
		try(Session session = this.sessionFactory.openSession()){
			Transaction tx = session.beginTransaction();
			session.remove(c_u);
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public List<Invitation> getInvitationList(User user) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c.id, c.user_id from Contact c where c.contact_id = :user_id and accepted = false and blocked = false");
			query.setParameter("user_id", user.getId());
			List<Object[]> contact_db = (List<Object[]>) query.getResultList();
			
			if(contact_db != null && !contact_db.isEmpty()){
				List<Invitation> invitations = new ArrayList<>();
				
				for(Object[] contact_obj : contact_db){
					Invitation invitation = new Invitation();
					
					invitation.setInvitation_id((int) contact_obj[0]);
					invitation.setRequester_id((int) contact_obj[1]);
					
					invitations.add(invitation);
				}
				return invitations;
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
	public void updateContact(Contact contact) {
		Session session = null;
		
		try{
			session = this.sessionFactory.openSession();
			
			Transaction tx = session.beginTransaction();
			session.merge(contact);
			tx.commit();
			session.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	@Override
	public void updateConversationUser(Conversation_User c_u) {
		Session session = null;
		
		try{
			session = this.sessionFactory.openSession();
			
			Transaction tx = session.beginTransaction();
			session.merge(c_u);
			tx.commit();
			session.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	@Override
	public void createContact(Contact user_contact) {
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.persist(user_contact);
		tx.commit();
		session.close();
	}
	
	@Override
	public Contact getContactForUser(int id, int contact_id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c from Contact c where c.user_id = :id and id = :contact_id");
			query.setParameter("contact_id", contact_id);
			query.setParameter("id", id);
			
			List<Contact> contacts = (List<Contact>)query.getResultList();
			
			if(contacts != null && !contacts.isEmpty()){
				return contacts.get(0);
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
	public List<ContactStatus> listContact(int id) {
		Session session = this.sessionFactory.openSession();
		
		try{
			Query query = session.createQuery("select c, u.status_id, u.peerjs_id from Contact c left join User u on c.contact_id = u.id where user_id = :user_id and accepted = true");
			query.setParameter("user_id", id);
			
			List<Object[]> contacts_db = (List<Object[]>)query.getResultList();
			
			if(contacts_db != null && !contacts_db.isEmpty()){
				List<ContactStatus> contacts = new ArrayList<>();
				
				for(Object[] contact_obj : contacts_db){
					ContactStatus contact_status = new ContactStatus();
					contact_status.setContact((Contact) contact_obj[0]);
					contact_status.setStatus((int) contact_obj[1]);
					contact_status.setPeerjs_id((String) contact_obj[2]);
					
					contacts.add(contact_status);
				}
				return contacts;
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
	public List<ContactSearch> searchContact(User user, String search) {
		Session session = this.sessionFactory.openSession();
		
		Query query;
		if(search != null && !search.isEmpty()){
			query = session.createQuery("select u.name, u.id, c.accepted from User u left join Contact c on c.user_id = u.id and c.contact_id = :id where u.id <> :id and u.name like :search");
			query.setParameter("id", user.getId());
			query.setParameter("search", "%"+search+"%");
		}else{
			query = session.createQuery("select u.name, u.id, c.accepted from User u left join Contact c on c.user_id = u.id and c.contact_id = :id where u.id <> :id");
			query.setParameter("id", user.getId());
		}
		query.setMaxResults(100);
		
		List<Object[]> user_db = (List<Object[]>) query.getResultList();
		
		if(user_db != null && !user_db.isEmpty()){
			List<ContactSearch> contacts = new ArrayList<>();
			
			for(Object[] contact_obj : user_db){
				ContactSearch contact_search = new ContactSearch();
				contact_search.setName((String) contact_obj[0]);
				contact_search.setId((int) contact_obj[1]);
				contact_search.setAccepted(contact_obj[2] != null && (boolean)contact_obj[2]);
				
				contacts.add(contact_search);
			}
			return contacts;
		}else{
			return null;
		}
	}
}
