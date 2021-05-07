package org.atouma.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Message;
import org.atouma.factory.Log;
import org.atouma.utils.CassandraUtil;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;

@Log
public class MessageDaoImpl implements MessageDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static Logger log = LogManager.getLogger(MessageDaoImpl.class);

	@Override
	public void sendMessage(Message m) {
		log.trace("attempting to query message table");
		String query = "Insert into messages (messageId, "
				+ " recipientId, "
				+ "senderId, header, body) "
				+ "values (?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s)
				.bind(m.getMessageId(),
				m.getRecipientId(),
				m.getSenderId(),
				m.getHeader(),
				m.getBody());
		session.execute(bound);
	}

}
