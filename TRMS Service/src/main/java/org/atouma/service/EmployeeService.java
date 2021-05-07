package org.atouma.service;

import org.atouma.beans.Employee;

public interface EmployeeService {

	Employee getEmployeeById(int employeeId);
	
	boolean addEmployee(Employee e);
	
	public boolean updateEmployee(Employee e);

}