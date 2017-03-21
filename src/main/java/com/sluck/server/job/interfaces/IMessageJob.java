package com.sluck.server.job.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;

public interface IMessageJob {
	public Conversation createConversation(Conversation conversation, User user);
	public void joinConversation(int conversation_id, User user);
	public List<Conversation> getConversationList(User user);
	public List<Conversation> searchConversation(String search);
	public Message sendMessage(User user, Message message, int conversation_id);
	public List<Message> listMessages(User user, int conversation_id, int message_id);
	public User getUserDetail(int id);
	public void addContact(User user, int contact_id) throws Exception;
	public List<Contact> getInvitationList(User user);
	public void updateInvitation(User user, int contact_id, boolean accept);
	public List<Contact> listContact(int id);
}
