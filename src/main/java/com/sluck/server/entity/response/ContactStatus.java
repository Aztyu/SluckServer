package com.sluck.server.entity.response;

import com.sluck.server.entity.Contact;

public class ContactStatus {
	private Contact contact;
	private int status;
	
	
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
