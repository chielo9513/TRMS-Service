package org.atouma.service;

import java.util.List;

import org.atouma.beans.Employee;
import org.atouma.beans.Request;

public interface RequestService {

	boolean submitRequest(Request r, Employee e);

	public List<Request> getAssignedRequests(int employeeId);
	//should be eid?
	List<Request> getRequestsByEmployee(Employee e);
	
	public boolean updateRequest(Request request);
	
	boolean approveRequest(Employee over, int employeeId, int requestId, boolean approval);

	Request getRequestById(int rid);

}