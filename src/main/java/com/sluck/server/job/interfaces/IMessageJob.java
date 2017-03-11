package com.sluck.server.job.interfaces;

import java.util.List;

import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.User;

public interface IMessageJob {
	public Conversation createConversation(Conversation conversation, User user);

	public void joinConversation(int conversation_id, User user);

	public List<Conversation> getConversationList(User user);
}
