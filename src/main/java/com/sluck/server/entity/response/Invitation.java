package com.sluck.server.entity.response;

public class Invitation {
	private int invitation_id;
	private int requester_id;
	
	public int getInvitation_id() {
		return invitation_id;
	}
	public void setInvitation_id(int invitation_id) {
		this.invitation_id = invitation_id;
	}
	public int getRequester_id() {
		return requester_id;
	}
	public void setRequester_id(int requester_id) {
		this.requester_id = requester_id;
	}
}
