package org.atouma.beans;

public class Message {
	private int messageId;
	private int recipientId;
	private int senderId;
	private String header;
	private String body;
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + messageId;
		result = prime * result + recipientId;
		result = prime * result + senderId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (messageId != other.messageId)
			return false;
		if (recipientId != other.recipientId)
			return false;
		if (senderId != other.senderId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", recipientId=" + recipientId + ", senderId=" + senderId
				+ ", header=" + header + ", body=" + body + "]";
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
