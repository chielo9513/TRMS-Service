package org.atouma.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Message;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;
import org.atouma.service.MessageService;
import org.atouma.service.MessageServiceImpl;

import io.javalin.http.Context;

@Log
public class MessageController {
	private static MessageService mServ = (MessageService) BeanFactory.getFactory().get(MessageService.class, MessageServiceImpl.class);
	private static final Logger log = LogManager.getLogger(MessageController.class);
	
	public static void sendMessage(Context ctx) {
		log.trace("sendMessage called");
		if(ctx.sessionAttribute("Employee") == null) {
			log.warn("You must log in to send messages");
		}
		Employee sender = ctx.sessionAttribute("Employee");
		Message m = ctx.bodyAsClass(Message.class);
		if(sender.getEmployeeId() == m.getRecipientId()) {
			ctx.status(400);
			return;
		}
		boolean sent = mServ.sendMessage(m);
		if(sent) {
			ctx.status(200);
		} else {
			ctx.status(409);
		}
	}
	
	public static void checkMessages(Context ctx) {
		log.trace("checking employee messages");
		
	}
}
