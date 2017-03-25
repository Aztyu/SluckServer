package com.sluck.server.job.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;
import com.sluck.server.dao.interfaces.IUserDAO;
import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;
import com.sluck.server.entity.response.ContactSearch;
import com.sluck.server.job.interfaces.IMessageJob;

@Component
public class MessageJob implements IMessageJob{
	@Autowired
	IMessageDAO message_dao;
	
	@Autowired
	IUserDAO user_dao;

	@Override
	public User getUserDetail(int id) {
		return user_dao.getUserDetail(id);
	}
	
	@Override
	public Conversation createConversation(Conversation conversation, User user) {
		conversation = message_dao.createConversation(conversation);
		message_dao.addUserToConversation(conversation, user);
		return conversation;
	}

	@Override
	public void joinConversation(int conversation_id, User user) {
		Conversation conversation = message_dao.getConversation(conversation_id);
		
		if(conversation.isShared()){	//Si la conversation n'est pas partagé/public on ne peut pas la rejoindre comme ça
			message_dao.addUserToConversation(conversation, user);
		}
	}
	
	@Override
	public List<Conversation> getConversationList(User user) {
		return message_dao.getConversationList(user);
	}
	
	@Override
	public List<Conversation> searchConversation(String search) {
		return message_dao.searchConversation(search);
	}
	
	@Override
	public Message sendMessage(User user, Message message, int conversation_id) {
		Conversation conversation = message_dao.hasConversationAccess(user, conversation_id);		//On vérifie les droits sur la conversation
		if(conversation != null){
			message.setUser_id(user.getId());					//On compléte les informations sur le message
			message.setConversation_id(conversation.getId());
			return message_dao.sendMessage(message);			//On sauvegarde les message dans la BDD
		}else{
			return null;
		}
	}
	
	@Override
	public List<Message> listMessages(User user, int conversation_id, int message_id) {
		Conversation conversation = message_dao.hasConversationAccess(user, conversation_id);		//On vérifie les droits sur la conversation
		if(conversation != null){
			return message_dao.listMessages(conversation.getId(), message_id);
		}else{
			return null;
		}
	}
	
	@Override
	public void addContact(User user, int contact_id) throws Exception {
		User contact_user = user_dao.getUserDetail(contact_id);
		
		if(user != null){
			boolean isContact = message_dao.isContact(user.getId(), contact_id);
			
			if(!isContact){	//Si il y a déjà une demande on n'en refait pas d'autre
				message_dao.createContactRequest(user.getId(), contact_user);
			}
		}else{
			throw new Exception("Le contact est inexistant");
		}	
	}
	
	@Override
	public List<Contact> getInvitationList(User user) {
		return message_dao.getInvitationList(user);
	}
	
	@Override
	public void updateInvitation(User user, int contact_id, boolean accept) {
		Contact contact = message_dao.getContactForUser(user.getId(), contact_id);
		
		contact.setAccepted(accept);		//Si on accepte alors accepted passe à true
		contact.setBlocked(!accept);		//Si on refuse accept passe à false et blocked à true
		
		message_dao.updateContact(contact);		//On mets à jour le contact
		
		User contact_user = user_dao.getUserDetail(contact.getUser_id());	//On récupére l'utilisateur à l'origine de la demande
		
		Contact user_contact = new Contact();		//On crée le contact dans le sens opposé
		user_contact.setUser_id(user.getId());
		user_contact.setName(contact_user.getName());
		user_contact.setContact_id(contact_user.getId());
		user_contact.setAccepted(accept);			
		user_contact.setBlocked(!accept);			
		
		message_dao.createContact(user_contact);
	}
	
	@Override
	public List<Contact> listContact(int id) {
		return message_dao.listContact(id);
	}
	
	@Override
	public void renameContact(int user_id, User contact) throws Exception {
		Contact contact_db = message_dao.getContactForUser(user_id, contact.getId());
		
		if(contact_db != null){
			contact_db.setName(contact.getName());		//On change le nom du contact
			message_dao.updateContact(contact_db);
		}else{
			throw new Exception("Contact inexistant");
		}
	}
	
	@Override
	public List<ContactSearch> searchContact(User user, String search) {
		return message_dao.searchContact(user, search);
	}
}
