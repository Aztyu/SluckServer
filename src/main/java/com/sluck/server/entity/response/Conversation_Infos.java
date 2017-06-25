package com.sluck.server.entity.response;

import com.sluck.server.entity.Conversation;

public class Conversation_Infos {
	private int id;
	private String name;
	private boolean shared;
	private boolean chat;		//Si on utilise cette conversation pour le chat
	private boolean admin;
	
	public Conversation_Infos() {}
	
	public Conversation_Infos(Conversation conv, boolean admin){
		this.id = conv.getId();
		this.name = conv.getName();
		this.shared = conv.isShared();
		this.chat = conv.isChat();
		this.admin = admin;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	public boolean isChat() {
		return chat;
	}
	public void setChat(boolean chat) {
		this.chat = chat;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
