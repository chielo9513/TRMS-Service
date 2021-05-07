package org.atouma.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Request;
import org.atouma.beans.Role;
import org.atouma.beans.Status;
import org.atouma.data.EmployeeDao;
import org.atouma.data.EmployeeDaoImpl;
import org.atouma.data.RequestDao;
import org.atouma.data.RequestDaoImpl;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;

import io.javalin.http.ForbiddenResponse;

@Log
public class RequestServiceImpl implements RequestService {
	private static Logger log = LogManager.getLogger(EmployeeServiceImpl.class);
	private RequestDao rd = (RequestDao) BeanFactory.getFactory().get(RequestDao.class, RequestDaoImpl.class);
	private EmployeeDao ed = (EmployeeDao) BeanFactory.getFactory().get(EmployeeDao.class, EmployeeDaoImpl.class);
	
	@Override
	public boolean submitRequest(Request r, Employee e) {
		log.trace("RequestServ: forwarding submitRequest request to rdao for "+e.getEmployeeId());
		try {
			rd.submitRequest(r, e);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("Request submission failed.");
			return false;
		}
	}
	
	@Override
	public boolean updateRequest(Request request) {
		try {
			rd.updateRequest(request);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean approveRequest(Employee over, int employeeId, int requestId, boolean approval) {
		Employee e = ed.getEmployeeById(employeeId);
		Request r = rd.getRequest(e, requestId);
		log.trace("checking validity of approval; "+over+"is attempting to approve request id: "+r.getRequestId()+"of employee "+e);
		if(over.getEmployeeId() == employeeId) {
			throw new ForbiddenResponse("Employee attempted to approve their own request.  Shame.");
		}
		try {
			if(e.getSupervisor() == over.getEmployeeId()) {
				r.setSuperApproved(approval);
				if(over.getEmployeeId() == e.getDeptHead()) {
					r.setDeptApproved(approval);
					if(over.getEmployeeId() == r.getAssignedBenCoId()) {
						r.setBenCoApproved(approval);
					}
				}
				log.trace("Updating request "+r.toString());
				return rd.updateRequest(r);
			}
			if(e.getDeptHead() == over.getEmployeeId()) {
				r.setDeptApproved(approval);
				if(over.getRole().equals(Role.BENCO)) {
					r.setBenCoApproved(approval);
					return rd.updateRequest(r);
				}
				log.trace("Updating request "+r.toString());
				return rd.updateRequest(r);
				
			}
			if(over.getRole().equals(Role.BENCO)) {
				if(r.isSuperApproved() && r.isDeptApproved()) {
					r.setBenCoApproved(approval);
					log.trace("Updating request "+r.toString());
					if(r.isBenCoApproved()) {
						r.setStatus(Status.PENDING);
						log.trace("Request status is now PENDING");
					}
					return rd.updateRequest(r);
				}
			}
			log.trace("Could not approve request stupit.");
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Something went wrong while trying to update "+r.toString());
			return false;
		}
	}

	
	
	@Override
	public List<Request> getRequestsByEmployee(Employee e) {
		log.trace("RequestServ: forwarding viewAllRequests request to rdao for employee id: "+e.getEmployeeId());
		try {
			List<Request> reqList = rd.getRequestsByEmployee(e);
			return reqList;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("Failure to retrieve requests list for "+e.getEmployeeId());
		}
		return null;
	}
	
	@Override
	public Request getRequestById(int rid) {
		log.trace("RequestServ: forwarding viewRequestById for "+rid);
		try {
			Request reqById = rd.viewRequestById(rid);
			return reqById; 
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("viewRequestById failed.");
		}
		return null;
	}
	@Override
	public List<Request> getAssignedRequests(int employeeId) {
		try {
			return rd.getAssignedRequests(employeeId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
