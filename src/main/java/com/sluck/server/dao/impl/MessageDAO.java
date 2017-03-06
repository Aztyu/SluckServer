package com.sluck.server.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.sluck.server.dao.interfaces.IMessageDAO;

@Component
public class MessageDAO implements IMessageDAO{
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	
}
