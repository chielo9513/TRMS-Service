package org.atouma.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.data.EmployeeDao;
import org.atouma.data.EmployeeDaoImpl;
import org.atouma.factory.BeanFactory;
import org.atouma.factory.Log;

@Log
public class EmployeeServiceImpl implements EmployeeService {
	private static Logger log = LogManager.getLogger(EmployeeServiceImpl.class);
	private EmployeeDao edao = (EmployeeDao) BeanFactory.getFactory().get(EmployeeDao.class, EmployeeDaoImpl.class);	
	
//	public EmployeeServiceImpl(EmployeeDao edao) {
//		this.edao = edao;
//	}
	
	
	public EmployeeServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public Employee getEmployeeById(int employeeId) {
		Employee e = null;
		try {
			e = edao.getEmployeeById(employeeId);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return e;		
	}
	
	@Override
	public boolean addEmployee(Employee e) {
		// TODO Auto-generated method stub
		try {
			edao.addEmployee(e);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("User already exists "+e.getEmployeeId());
			return false;
		}
	}


	@Override
	public boolean updateEmployee(Employee e) {
		try {
			edao.updateEmployee(e);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
