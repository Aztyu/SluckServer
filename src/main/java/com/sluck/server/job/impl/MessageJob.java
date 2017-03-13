package com.sluck.server.job.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;
import com.sluck.server.dao.interfaces.IUserDAO;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;
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
}
