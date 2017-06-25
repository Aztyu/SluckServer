package com.sluck.server.dao.interfaces;

import java.util.List;

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

public interface IMessageDAO {
	public void addUserToConversation(int conversation_id, int user_id, boolean admin, boolean moderator);
	public Conversation createConversation(Conversation conversation);
	public Conversation getConversation(int conversation_id);
	public List<Conversation_Infos> getConversationList(User user);
	public List<Conversation> searchConversation(String search, int user_id);
	public Conversation hasConversationAccess(User user, int conversation_id);
	public Message sendMessage(Message message);
	public List<Message> listMessages(int id, int message_id);
	public boolean isContact(int id, int contact_id);
	public Contact createContactRequest(int id, User contact_user);
	public List<Invitation> getInvitationList(User user);
	public Contact getContactForUser(int user_id, int contact_id);
	public void createContact(Contact user_contact);
	public List<ContactStatus> listContact(int id);
	public void updateContact(Contact contact_db);
	public List<ContactSearch> searchContact(User user, String search);
	public void quitConversation(Conversation_User conversation_user);
	public Conversation_User getConversationUser(int conversation_id, int id);
	public void removeContactRequest(int id, User contact_user);
	public void saveMessageFile(MessageFile file_obj);
	public MessageFile getMessageFile(int id);
	public Conversation findChatConversation(int user_id, int contact_id);
	public void saveConversationInvitation(Conversation_Invitation conv_inv);
	public List<Conversation_Invitation> getConversationInvitationList(User user);
	public Conversation_Invitation getConversationInvitation(int invitation_id);
	public void removeContactInvitation(int invitation_id);
	public boolean hasConversationAdmin(int id, int conversation_id);
	public void updateConversationUser(Conversation_User c_u);
	public void removeConversationUser(Conversation_User c_u);
	public Message getMessage(int message_id);
	public void removeMessage(Message message);
}
