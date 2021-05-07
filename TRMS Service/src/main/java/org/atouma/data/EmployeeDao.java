package org.atouma.data;

import java.util.List;

import org.atouma.beans.Employee;

public interface EmployeeDao {

	//registering a user, putting them in the database
	boolean addEmployee(Employee e);

	//return employee object by searching for id
	Employee getEmployeeById(int employeeId);

	//return list of employees associated with  
	List<Employee> getEmployees(Employee over);

	List<Employee> getEmployees();
	
	void updateEmployee(Employee e);

	void updateRequests();

	//void updateRequests();

}