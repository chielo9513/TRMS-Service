package org.atouma.data;

import java.util.List;

import org.atouma.beans.Employee;
import org.atouma.beans.Request;
import org.atouma.beans.Status;

public interface RequestDao {

	//a logged in user making a request
	void submitRequest(Request r, Employee e);
	
	Request viewRequestById(int rid);
	
	List<Request> getRequests();
	
	List<Request> getRequestsByEmployee(Employee e);
	
	//viewing all requests
	
	List<Request> viewAllRequests(Employee e, Status s);
	
	void timeoutRequests();

	boolean updateRequest(Request r);
	
	Request getRequest(Employee e, int requestId);

	List<Request> getAssignedRequests(int employeeId);

}