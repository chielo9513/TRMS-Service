package org.atouma.controllers;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Role;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;
import org.atouma.service.EmployeeService;
import org.atouma.service.EmployeeServiceImpl;
import org.atouma.utils.S3Util;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.NotFoundResponse;

@Log
public class EmployeeController  {
	private static EmployeeService empServ = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, 
			EmployeeServiceImpl.class);
	//private static S3Util s3u = 
	private static final Logger log = LogManager.getLogger(EmployeeController.class);
	
	//POST to employees
	public static void addEmployee(Context ctx) {
		Employee e = new Employee();
		e.setEmployeeId(Integer.parseInt(ctx.formParam("employeeId")));
		e.setEmployeeEmail(ctx.formParam("employeeEmail"));
		e.setName(ctx.formParam("name"));
		e.setSupervisor(Integer.parseInt(ctx.formParam("supervisor")));
		e.setDeptHead(Integer.parseInt(ctx.formParam("deptHead")));
		e.setRole(Role.valueOf(ctx.formParam("role")));
		log.trace("EmpCon: forwarding to empServ: add employee "+e.toString());
		boolean added = empServ.addEmployee(e);
		if (added) {
			ctx.json(e);
		} else {
			ctx.status(409);
		}
	}
	//TODO
	public static void login(Context ctx) {
		if (ctx.sessionAttribute("Employee") != null) {
			ctx.status(204);
			return;
		}
		int employeeId = Integer.parseInt(ctx.formParam("employeeId"));
		Employee e = empServ.getEmployeeById(employeeId);
		if(e == null) {
			ctx.status(401);
		} else {
			log.trace("Logged in before as "+e.toString());
			ctx.sessionAttribute("Employee", e);
			ctx.json(e);
			log.trace("Logged in as "+e.toString());
		}
	}
	
	public void updateEmployee(Context ctx ) {
		log.trace("get Employee by id");
		
		int eid = 0;
		Employee employee;
		Employee current;
		Employee user = ctx.sessionAttribute("User");
		
		try {
			eid = Integer.parseInt(ctx.pathParam("employeeId"));
			employee = ctx.bodyAsClass(Employee.class);
		} catch(Exception ex) {
			throw new BadRequestResponse();
		}
		
		current = empServ.getEmployeeById(eid);
		if (current == null) {
			throw new NotFoundResponse();
		}
		
		if(user.getEmployeeId() != current.getEmployeeId() && user.getEmployeeId() != current.getSupervisor()) {
			log.warn("Forbidden: " + "user.getEmployeeId() tried update a different user: " +current.getEmployeeId());
			throw new ForbiddenResponse();
		}
		if (user.getEmployeeId() == current.getEmployeeId() && (employee.getRole() != null || employee.getSupervisor() != 0)) {
			log.warn("Forbidden: " + user.getName() + " tried to update themselves to " + employee.toString());
			throw new ForbiddenResponse();
		}

		
	}
	
	public static void logout(Context ctx) {
		log.trace("Logging out.");
		ctx.req.getSession().invalidate();
	}
//	
//	public static void uploadPicture(Context ctx) {
//		String name = new StringBuilder(ctx.pathParam("name")).append(".jpg").toString();
//		byte[] bytes = ctx.bodyAsBytes();
//		try {
//			S3Util.getInstance().uploadToBucket(name, bytes);
//		} catch (Exception e) {
//			ctx.status(500);
//		}
//		ctx.status(204);
//	}
	
	public static void getPicture(Context ctx) {

		String name = new StringBuilder(ctx.pathParam("name")).append(".jpg").toString();
		try {
			InputStream s = S3Util.getInstance().getObject(name);
			ctx.result(s);
		} catch(Exception e) {
			ctx.status(500);
		}
	}
	
	public static void awardReimbursement(Context ctx) {
		//TODO
		
	}
	
	public void getEmployeeById(Context ctx) {
		log.trace("get Employee by id");
		
		int eid = 0;
		try {
			eid = Integer.parseInt(ctx.pathParam("employeeId"));
		} catch (NumberFormatException e) {
			log.warn("Invalid id input");
			throw new BadRequestResponse("Enter a number for id");
		}
		
		Employee e = empServ.getEmployeeById(eid);
		if (e == null) {
			log.warn("Employee was not found for id "+eid);
			throw new NotFoundResponse();
		}
		
		ctx.json(e);
	}
}

