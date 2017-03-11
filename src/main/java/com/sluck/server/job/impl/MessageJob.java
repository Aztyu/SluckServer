package com.sluck.server.job.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.User;
import com.sluck.server.job.interfaces.IMessageJob;

@Component
public class MessageJob implements IMessageJob{
	@Autowired
	IMessageDAO message_dao;

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
}
