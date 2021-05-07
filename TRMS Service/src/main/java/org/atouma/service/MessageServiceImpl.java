package org.atouma.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Message;
import org.atouma.data.MessageDao;
import org.atouma.data.MessageDaoImpl;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;

@Log
public class MessageServiceImpl implements MessageService {
	private static Logger log = LogManager.getLogger(MessageServiceImpl.class);
	private MessageDao mdao = (MessageDao) BeanFactory.getFactory().get(MessageDao.class, MessageDaoImpl.class);
	public boolean sendMessage(Message m) {
		
		try {
			mdao.sendMessage(m);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("mService sendMessage threw an exception");
			return false;
		}
		
	}
}