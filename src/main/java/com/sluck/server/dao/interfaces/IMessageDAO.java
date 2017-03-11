package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.User;

public interface IMessageDAO {
	public void addUserToConversation(Conversation conversation, User user);
	public Conversation createConversation(Conversation conversation);
	public Conversation getConversation(int conversation_id);
	public List<Conversation> getConversationList(User user);
}
