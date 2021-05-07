package org.atouma.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.EventType;
import org.atouma.beans.GradingFormat;
import org.atouma.beans.Request;
import org.atouma.beans.Role;
import org.atouma.beans.Status;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;
import org.atouma.service.RequestService;
import org.atouma.service.RequestServiceImpl;

import io.javalin.http.Context;

@Log
public class RequestController {
	private static RequestService rs = (RequestService) BeanFactory.getFactory().get(RequestService.class, 
			RequestServiceImpl.class);
	private static final Logger log = LogManager.getLogger(RequestController.class);
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public static void submitRequest(Context ctx) {
		if(ctx.sessionAttribute("Employee") == null) {
			ctx.status(401);
			log.trace("You must be logged in to submit a reimbursement request.");
			return;
		}
		Employee e = ctx.sessionAttribute("Employee");
		Request r = new Request();
		r.setType(EventType.valueOf(ctx.formParam("type").toUpperCase()));
		r.setStatus(Status.SUBMITTED);
		r.setPassProof(Boolean.parseBoolean(ctx.formParam("passProof")));
		r.setAssignedBenCoId(Integer.parseInt(ctx.formParam("assignedBenCoId")));
		r.setRequestId(Integer.parseInt(ctx.formParam("requestId")));
		r.setEmployeeId(Integer.parseInt(ctx.pathParam("employeeId")));
		r.setApplicationTimeZone(ctx.formParam("applicationTimeZone"));
		r.setEventLocation(ctx.formParam("eventLocation"));
		r.setEventDescription(ctx.formParam("eventDescription"));
		LocalDateTime localEventDate = LocalDateTime.parse(ctx.formParam("eventDateTime"), formatter);
		ZoneId eventTimeZone = ZoneId.of(ZoneId.SHORT_IDS.get(ctx.formParam("eventTimeZone")));
		ZonedDateTime eventDateTime = ZonedDateTime.of(localEventDate, eventTimeZone);
		r.setEventTimeZone(eventTimeZone.toString());
		r.setEventDateTime(eventDateTime);
		r.setEventCost(Float.parseFloat(ctx.formParam("eventCost")));
		r.setEventGradingFormat(GradingFormat.valueOf(ctx.formParam("eventGradingFormat")));
		r.setReason(ctx.formParam("reason"));
		log.trace("rc: forwarding to rs, submit request for "+e.getEmployeeId());
		log.trace("rc: request being submitted: "+r.toString());
		boolean submitted = rs.submitRequest(r, e);
		if(submitted) {
			ctx.json(r);
		}
		else {
			ctx.status(409);
			log.warn("RCON: request submission failed.");
		}
	}
	
//	public static void cancelRequest(Context ctx) {
//		if(ctx.sessionAttribute("Employee") == null) {
//			ctx.status(405);
//			log.warn("You must be logged in to cancel a reimbursement request.");
//			return;
//		}
//		Employee e = ctx.sessionAttribute("Employee");
//		//need validation here such that an employee can only cancel their own requests
//		//if(e.getEmployeeId() == ctx.)
//		int rid = Integer.parseInt(ctx.formParam("requestId"));
//		try {
//			boolean cancelled = rs.cancelRequest(e, rid);
//			if(cancelled) {
//				ctx.status(204);
//			}
//		} catch (Exception x) {
//			x.printStackTrace();
//			ctx.status(400);
//		}
//	}
	
	public static void viewRequestById(Context ctx) {
		if(ctx.sessionAttribute("Employee") == null) {
			ctx.status(401);
			log.warn("Not logged in.");
		}
		Employee e = ctx.sessionAttribute("Employee");
		int rid = Integer.parseInt(ctx.formParam("requestId"));
		log.trace(e);
		if(e.getRole().equals(Role.SUPERVISOR) 
				| e.getRole().equals(Role.DEPT_HEAD)
				| e.getRole().equals(Role.BENCO)) {
			try {
				Request r = rs.getRequestById(rid);
				ctx.status(200);
				ctx.json(r);
				return;
			} catch(Exception ex) {
				ex.printStackTrace();
				ctx.status(400);
			}
		} else {
			log.error("Attempted to view request for which user did not have permission");
			ctx.status(403);
		}
	}
	
	//GET /employees
	public static void viewAllRequests(Context ctx) {
		if(ctx.sessionAttribute("Employee") == null) {
			ctx.status(401);
			log.warn("Not logged in.");
			return;
		}
		Employee e = ctx.sessionAttribute("Employee");
		try {
			List<Request> allReqs = rs.getRequestsByEmployee(e);
			if(allReqs == null | allReqs.isEmpty()) {
				ctx.status(204);
				return;
			} else {
				ctx.status(200);
				ctx.json(allReqs);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			ctx.status(400);
		}
	}
	
	public static void approveRequest(Context ctx) {
		if(ctx.sessionAttribute("Employee") == null) {
			ctx.status(401);
			log.warn("Not logged in.");
			return;
			//TODO over validation?
		}
		Employee over = ctx.sessionAttribute("Employee");
		log.trace(over);
		int eid = Integer.parseInt(ctx.formParam("employeeId"));
		int rid = Integer.parseInt(ctx.formParam("requestId"));
		boolean approval = Boolean.parseBoolean(ctx.formParam("approval"));
		log.trace(approval);
		try {
			boolean approved = rs.approveRequest(over, eid, rid, approval);
			if(approved){
				ctx.status(200);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			ctx.status(400);
		}
	}

}
