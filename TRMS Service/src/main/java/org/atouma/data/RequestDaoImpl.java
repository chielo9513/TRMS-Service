package org.atouma.data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.EventType;
import org.atouma.beans.GradingFormat;
import org.atouma.beans.Request;
import org.atouma.beans.Status;
import org.atouma.factory.Log;
import org.atouma.utils.CassandraUtil;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;

@Log
public class RequestDaoImpl implements RequestDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static Logger log = LogManager.getLogger(RequestDaoImpl.class);

	public void submitRequest(Request r, Employee e) {
		log.trace(r.toString()+e.toString());
//		List<Request> requests = getRequests();
//		int numOfRequests = 0;
//		for(Request request: requests) {
//			numOfRequests++;
//		}
		ZoneId appZ = ZoneId.of(ZoneId.SHORT_IDS.get(r.getApplicationTimeZone()));
		ZonedDateTime submissionTime = ZonedDateTime.now(appZ);
		
		String query = "Insert into requests (requestId, employeeId, assignedBenCoId, " 
				+ "eventLocation, eventDescription, eventTimeZone, "
				+ "eventDateTime, eventCost, eventGradingFormat, reason, "
				+ "status, type, passProof, applicationTimeZone, " 
				+ "applicationDateTime, isUrgent, "
				+ "superApproved, deptApproved, benCoApproved, projectedAward, awardValue) " 
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s)
				.bind(r.getRequestId(), 
				r.getEmployeeId(),
				r.getAssignedBenCoId(),
				r.getEventLocation(), 
				r.getEventDescription(), 
				r.getEventTimeZone(),
				r.getEventDateTime(),
				r.getEventCost(),
				r.getEventGradingFormat().toString(),
				r.getReason(), 
				r.getStatus().toString(),
				r.getType().toString(),
				r.isPassProof(),
				appZ.toString(),
				submissionTime,
				r.isUrgent(),
				r.isSuperApproved(),
				r.isDeptApproved(),
				r.isBenCoApproved(),
				r.getProjectedAward(),
				r.getAwardValue());
		session.execute(bound);
	}
	
	@Override
	public Request viewRequestById(int rid) {
		Request req = null;
		
		String query = "SELECT * from requests WHERE requestId = ?;";
		BoundStatement bound = session.prepare(query).bind(rid);
		ResultSet rs = session.execute(bound);
		
		Row data = rs.one();
		if(data != null) {
			ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
            ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));
            
			req = new Request();
			req.setType(EventType.valueOf(data.getString("type")));
			req.setStatus(Status.valueOf(data.getString("status")));
			req.setRequestId(data.getInt("requestId"));
			req.setAssignedBenCoId(data.getInt("assignedBenCoId"));
			req.setEmployeeId(data.getInt("employeeId"));
			req.setApplicationTimeZone(appZ.toString());
			req.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
			req.setEventLocation(data.getString("eventLocation"));
			req.setEventDescription(data.getString("eventDescription"));
			req.setEventTimeZone(eventZ.toString());
			req.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
			req.setEventCost(data.getFloat("eventCost"));
			req.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
			req.setReason(data.getString("reason"));
			req.setUrgent(data.getBoolean("isUrgent"));
			req.setSuperApproved(data.getBoolean("superApproved"));
			req.setDeptApproved(data.getBoolean("deptApproved"));
			req.setBenCoApproved(data.getBoolean("benCoApproved"));
			req.setProjectedAward(data.getFloat("projectedAward"));
			req.setAwardValue(data.getFloat("awardValue"));
			req.setPassProof(data.getBoolean("passProof"));
	        log.trace("Request added to list: "+req.toString());
		}
		return req;
	}
	//TODO
	
	public List<Request> getRequests() {
		List<Request> reqList = new ArrayList<Request>();
		try {
			String query = "SELECT * FROM requests ALLOW FILTERING;";
			ResultSet rs = session.execute(query);
			
			rs.forEach(data -> {
				ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
	            ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));
				
				Request req = new Request();
				req.setType(EventType.valueOf(data.getString("type")));
				req.setStatus(Status.valueOf(data.getString("status")));
				req.setRequestId(data.getInt("requestId"));
				req.setAssignedBenCoId(data.getInt("assignedBenCoId"));
				req.setEmployeeId(data.getInt("employeeId"));
				req.setApplicationTimeZone(appZ.toString());
				req.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
				req.setEventLocation(data.getString("eventLocation"));
				req.setEventDescription(data.getString("eventDescription"));
				req.setEventTimeZone(eventZ.toString());
				req.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
				req.setEventCost(data.getFloat("eventCost"));
				req.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
				req.setReason(data.getString("reason"));
				req.setUrgent(data.getBoolean("isUrgent"));
				req.setSuperApproved(data.getBoolean("superApproved"));
				req.setDeptApproved(data.getBoolean("deptApproved"));
				req.setBenCoApproved(data.getBoolean("benCoApproved"));
				req.setProjectedAward(data.getFloat("projectedAward"));
				req.setAwardValue(data.getFloat("awardValue"));
				req.setPassProof(data.getBoolean("passProof"));
		        log.trace("Request added to list: "+req.toString());
				reqList.add(req);
			});
			
			return reqList;
		} catch(Exception ex) {
			log.warn("Something went wrong getting requests");
			return null;
		}
		
	}
	@Override
	public List<Request> getRequestsByEmployee(Employee e) {
		List<Request> reqList = new ArrayList<Request>();
		
		String query = "SELECT * FROM requests WHERE employeeId = ? ALLOW FILTERING;";
		BoundStatement bound = session.prepare(query).bind(e.getEmployeeId());
		ResultSet rs = session.execute(bound);
		rs.forEach(data -> {
			ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
            ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));
			
			Request r = new Request();
			r.setType(EventType.valueOf(data.getString("type")));
	        r.setStatus(Status.valueOf(data.getString("status")));
	        r.setRequestId(data.getInt("requestId"));
	        r.setEmployeeId(data.getInt("employeeId"));
	        r.setApplicationTimeZone(appZ.toString());
	        r.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
	        r.setEventLocation(data.getString("eventLocation"));
	        r.setEventDescription(data.getString("eventDescription"));
	        r.setEventTimeZone(eventZ.toString());
	        r.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
	        r.setEventCost(data.getFloat("eventCost"));
	        r.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
	        r.setReason(data.getString("reason"));
	        r.setSuperApproved(data.getBoolean("superApproved"));
	        r.setDeptApproved(data.getBoolean("deptApproved"));
	        r.setBenCoApproved(data.getBoolean("benCoApproved"));
	        r.setProjectedAward(data.getFloat("projectedAward"));
	        r.setAwardValue(data.getFloat("awardValue"));
	        log.trace("Request added to list: "+r.toString());
			reqList.add(r);
		});
		
		return reqList;
	}
	
	@Override
	public List<Request> viewAllRequests(Employee e, Status s) {
		List<Request> reqList = new ArrayList<Request>();

		String query = "SELECT * FROM requests WHERE status = ? AND employeeId = ? ALLOW FILTERING;";
        BoundStatement bound = session.prepare(query).bind(s.toString(), e.getEmployeeId());
        ResultSet rs = session.execute(bound);

        rs.forEach(data -> {
            ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
            ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));

            Request r = new Request();
            r.setType(EventType.valueOf(data.getString("type")));
            r.setStatus(Status.valueOf(data.getString("status")));
            r.setRequestId(data.getInt("requestId"));
            r.setEmployeeId(data.getInt("employeeId"));
            r.setApplicationTimeZone(appZ.toString());
            r.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
            r.setEventLocation(data.getString("eventLocation"));
            r.setEventDescription(data.getString("eventDescription"));
            r.setEventTimeZone(eventZ.toString());
            r.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
            r.setEventCost(data.getFloat("eventCost"));
            r.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
            r.setReason(data.getString("reason"));
            r.setSuperApproved(data.getBoolean("superApproved"));
            r.setDeptApproved(data.getBoolean("deptApproved"));
            r.setBenCoApproved(data.getBoolean("benCoApproved"));
            r.setProjectedAward(data.getFloat("projectedAward"));
            r.setAwardValue(data.getFloat("awardValue"));
            r.setPassProof(data.getBoolean("passProof"));
            log.trace("Request added to list: "+r.toString());
            reqList.add(r);
        });

        return reqList;
    }
	
	public void timeoutRequests() {
		try {
			String query = "SELECT * FROM requests WHERE status = ? ALLOW FILTERING;";
			BoundStatement bound = session.prepare(query).bind(Status.SUBMITTED.toString());
			ResultSet rs = session.execute(bound);

			rs.forEach(data -> {
				ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
				ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));
				ZonedDateTime current = ZonedDateTime.now(appZ);
				
				Request r = new Request();
				r.setType(EventType.valueOf(data.getString("type")));
	            r.setStatus(Status.valueOf(data.getString("status")));
				r.setRequestId(data.getInt("requestId"));
				r.setEmployeeId(data.getInt("employeeId"));
				r.setApplicationTimeZone(appZ.toString());
				r.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
				r.setEventLocation(data.getString("eventLocation"));
				r.setEventDescription(data.getString("eventDescription"));
				r.setEventTimeZone(eventZ.toString());
				r.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
				r.setEventCost(data.getFloat("eventCost"));
				r.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
				r.setReason(data.getString("reason"));
				r.setPassProof(data.getBoolean("passProof"));
				log.trace("rdao timeoutRequests: current = "+current.toString()+", appDate = "+r.getApplicationDateTime().toString()+", appDate+7 days = "+r.getApplicationDateTime().plusDays(7).toString());
				if(current.isAfter(r.getApplicationDateTime().plusDays(7))) {
					r.setSuperApproved(true);
					r.setDeptApproved(true);
					log.trace("Super and dept head approval skipped duo to date proximity");
				}
				else {
					r.setSuperApproved(data.getBoolean("superApproved"));
					r.setDeptApproved(data.getBoolean("deptApproved"));
				}
				r.setBenCoApproved(data.getBoolean("benCoApproved"));
				r.setProjectedAward(data.getFloat("projectedAward"));
				r.setAwardValue(data.getFloat("awardValue"));
				updateRequest(r);
				
			});

			//return allForms;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public boolean updateRequest(Request r) {
		log.trace("updateRequest called Rdao");
		try {
		String query = "UPDATE requests set employeeId = ?, assignedBenCoId = ?, "
				+"eventLocation = ?, eventDescription = ?, eventTimeZone = ?, eventDateTime = ?, "
				+"eventCost = ?, eventGradingFormat = ?, reason = ?, status = ?, type = ?, "
				+"passProof = ?, applicationTimeZone = ?, applicationDateTime = ?, isUrgent = ?, "
				+"superApproved =?, deptApproved = ?, benCoApproved = ?, projectedAward = ?, awardValue = ? "
				+"WHERE requestId = ?";
			
		log.trace("trying statement");
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(
				r.getEmployeeId(),
				r.getAssignedBenCoId(),
				r.getEventLocation(),
				r.getEventDescription(), 
				r.getEventTimeZone(), 
				r.getEventDateTime(), 
				r.getEventCost(),
				r.getEventGradingFormat().toString(),
				r.getReason(), 
				r.getStatus().toString(), 
				r.getType().toString(), 
				r.isPassProof(),
				r.getApplicationTimeZone(),  
				r.getApplicationDateTime(),
				r.isUrgent(),
				r.isSuperApproved(), 
				r.isDeptApproved(), 
				r.isBenCoApproved(), 
				r.getProjectedAward(), 
				r.getAwardValue(),
				r.getRequestId()
				);
		session.execute(bound);
		log.trace("statement executed");
		return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

	@Override
	public Request getRequest(Employee e, int requestId) {
		Request r = null;
		String query = "SELECT * FROM requests WHERE employeeId = ? AND requestId = ? ALLOW FILTERING;";
		BoundStatement bound = session.prepare(query).bind(e.getEmployeeId(), requestId);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			r = new Request();
			
			ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
			ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));

			r.setPassProof(data.getBoolean("passProof"));
			r.setAssignedBenCoId(data.getInt("assignedBenCoId"));
			r.setType(EventType.valueOf(data.getString("type")));
            r.setStatus(Status.valueOf(data.getString("status")));
			r.setRequestId(data.getInt("requestId"));
			r.setEmployeeId(data.getInt("employeeId"));
			r.setApplicationTimeZone(appZ.toString());
			r.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
			r.setEventLocation(data.getString("eventLocation"));
			r.setEventDescription(data.getString("eventDescription"));
			r.setEventTimeZone(eventZ.toString());
			r.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
			r.setEventCost(data.getFloat("eventCost"));
			r.setEventGradingFormat(GradingFormat.valueOf(data.getString("eventGradingFormat")));
			r.setReason(data.getString("reason"));
			r.setSuperApproved(data.getBoolean("superApproved"));
			r.setDeptApproved(data.getBoolean("deptApproved"));
			r.setBenCoApproved(data.getBoolean("benCoApproved"));
			r.setProjectedAward(data.getFloat("projectedAward"));
			r.setAwardValue(data.getFloat("awardValue"));
		}

		return r;
	}

	@Override
	public List<Request> getAssignedRequests(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
	//TODO
//	@Override
//	public List<Request> getAssignedRequests(int employeeId) {
//		List<Request> requests = new ArrayList<>();
//		String query = "SELECT * FROM requests";
//		ResultSet results = session.execute(query);
//		
//		for (Row row : results) {
//			if(row.getInt(""))
//		}
//		return null;
//	}
//}
