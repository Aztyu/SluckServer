package com.sluck.server.dao.interfaces;

import java.util.List;

import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.User;
import com.sluck.server.entity.response.ContactSearch;

public interface IMessageDAO {
	public void addUserToConversation(Conversation conversation, User user);
	public Conversation createConversation(Conversation conversation);
	public Conversation getConversation(int conversation_id);
	public List<Conversation> getConversationList(User user);
	public List<Conversation> searchConversation(String search);
	public Conversation hasConversationAccess(User user, int conversation_id);
	public Message sendMessage(Message message);
	public List<Message> listMessages(int id, int message_id);
	public boolean isContact(int id, int contact_id);
	public Contact createContactRequest(int id, User contact_user);
	public List<Contact> getInvitationList(User user);
	public Contact getContactForUser(int user_id, int contact_id);
	public void createContact(Contact user_contact);
	public List<Contact> listContact(int id);
	public void updateContact(Contact contact_db);
	public List<ContactSearch> searchContact(User user, String search);
}
