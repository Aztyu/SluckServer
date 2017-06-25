package com.sluck.server.job.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sluck.server.entity.Contact;
import com.sluck.server.entity.Conversation;
import com.sluck.server.entity.Conversation_Invitation;
import com.sluck.server.entity.Message;
import com.sluck.server.entity.MessageFile;
import com.sluck.server.entity.User;
import com.sluck.server.entity.response.ContactSearch;
import com.sluck.server.entity.response.ContactStatus;
import com.sluck.server.entity.response.Conversation_Infos;
import com.sluck.server.entity.response.Invitation;

public interface IMessageJob {
	public Conversation createConversation(Conversation conversation, User user);
	public void joinConversation(int conversation_id, User user);
	public List<Conversation_Infos> getConversationList(User user);
	public List<Conversation> searchConversation(String search, int user_id);
	public Message sendMessage(User user, Message message, int conversation_id) throws IOException;
	public List<Message> listMessages(User user, int conversation_id, int message_id);
	public User getUserDetail(int id);
	public void addContact(User user, int contact_id) throws Exception;
	public List<Invitation> getInvitationList(User user);
	public void updateInvitation(User user, int contact_id, boolean accept);
	public List<ContactStatus> listContact(int id);
	public List<ContactSearch> searchContact(User user, String search);
	public void quitConversation(int conversation_id, User user);
	public void removeContact(User user, int contact_id) throws Exception;
	public MessageFile getMessageFile(int id);
	public List<Message> listChatMessages(User user, int contact_id, int message_id);
	public void inviteToConversation(User user, int conversation_id, int user_id);
	public List<Conversation_Invitation> getConversationInvitationList(User user);
	public void updateConvInvitation(User user, int invitation_id, boolean b);
	public void renameContact(int id, String name, int contact_id) throws Exception;
	public Message sendChatMessage(User user, Message message, int contact_id) throws IOException;
	public void makeUserModConversation(User user, int conversation_id, int user_id) throws Exception;
	public void banFromConversation(User user, int conversation_id, int user_id) throws Exception;
	public void kickFromConversation(User user, int conversation_id, int user_id) throws Exception;
	public void deleteMessage(int message_id);
	public void addBot(int conversation_id, int bot_id);
	public List<User> listBot();
}
