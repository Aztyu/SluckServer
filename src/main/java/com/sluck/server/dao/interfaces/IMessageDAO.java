package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;

public interface IMessageDAO {
	public void addUserToConversation(Conversation conversation, User user);
	public Conversation createConversation(Conversation conversation);
	public Conversation getConversation(int conversation_id);
	public List<Conversation> getConversationList(User user);
	public List<Conversation> searchConversation(String search);
	public Conversation hasConversationAccess(User user, int conversation_id);
	public Message sendMessage(Message message);
	public List<Message> listMessages(int id, int message_id);
}
