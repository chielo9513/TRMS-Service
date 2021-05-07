package org.atouma.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Request;
import org.atouma.beans.Role;
import org.atouma.beans.Status;
import org.atouma.factory.BeanFactory;
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
public class EmployeeDaoImpl implements EmployeeDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static Logger log = LogManager.getLogger(EmployeeDaoImpl.class);
	private RequestDao rdao = (RequestDao) BeanFactory.getFactory().get(RequestDao.class, RequestDaoImpl.class);

	@Override
	public boolean addEmployee(Employee e) {
		
		String query = "Insert into employees (employeeId, employeeEmail, name, "
				+ "supervisor, deptHead, role, "
				+ "totalReimbursements, pendingReimbursements, awardedReimbursements, "
				+ "availableReimbursements) values (?,?,?,?,?,?,?,?,?,?); ";
		
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s)
				.bind(e.getEmployeeId(),
				e.getEmployeeEmail(),
				e.getName(),
				e.getSupervisor(),
				e.getDeptHead(),
				e.getRole().toString(),
				null,
				null,
				null,
				(float) 1000f);
		session.execute(bound);
		return true;
	}

	public Employee getEmployeeById(int employeeId) {
		Employee e = null;
		String query = "SELECT * FROM employees WHERE employeeId = ?;";
		
		BoundStatement bound = session.prepare(query).bind(employeeId);
		ResultSet rs = session.execute(bound);
		
		Row data = rs.one();
		if(data != null) {
			e = new Employee();
			e.setEmployeeId(data.getInt("employeeId"));
			e.setEmployeeEmail(data.getString("employeeEmail"));
			e.setName(data.getString("name"));
			e.setSupervisor(data.getInt("supervisor"));
			e.setDeptHead(data.getInt("deptHead"));
			e.setRole(Role.valueOf(data.getString("role")));
			e.setTotalReimbursements(data.getFloat("totalReimbursements"));
			e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
			e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
			e.setAvailableReimbursements(data.getFloat("availableReimbursements"));
		}
		return e;
	}
	
	@Override
	public List<Employee> getEmployees(Employee over) {
		List<Employee> employees = new ArrayList<Employee>();
		try {
		String query = "SELECT * FROM employees WHERE directSuperId = ? OR deptHeadId = ? ALLOW FILTERING";
		BoundStatement bound = session.prepare(query).bind(over.getEmployeeId(), over.getEmployeeId());
		ResultSet rs = session.execute(bound);

		rs.forEach(data -> {
			Employee e = new Employee();
			e.setEmployeeId(data.getInt("employeeId"));
			e.setEmployeeEmail(data.getString("employeeEmail"));
			e.setName(data.getString("name"));
			e.setSupervisor(data.getInt("directSuper"));
			e.setDeptHead(data.getInt("deptHead"));
			e.setTotalReimbursements(data.getFloat("totalReimbursements"));
			e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
			e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
			Float available = e.getTotalReimbursements() - e.getPendingReimbursements() - e.getAwardedReimbursements();
			e.setAvailableReimbursements(available);
			employees.add(e);
		});

		return employees;
		} catch (Exception x) {
			log.warn("Nothing to see here folks");
			return null;
		}
	}

	@Override
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		try {
			String query = "SELECT * FROM employees ALLOW FILTERING";
			ResultSet rs = session.execute(query);
			
			rs.forEach(data -> {
				Employee e = new Employee();
				e.setEmployeeId(data.getInt("employeeId"));
				e.setEmployeeEmail(data.getString("employeeEmail"));
				e.setName(data.getString("name"));
				e.setSupervisor(data.getInt("directSuper"));
				e.setDeptHead(data.getInt("deptHead"));
				e.setTotalReimbursements(data.getFloat("totalReimbursements"));
				e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
				e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
				Float available = e.getTotalReimbursements() - e.getPendingReimbursements() - e.getAwardedReimbursements();
				e.setAvailableReimbursements(available);
				employees.add(e);
			});
			return employees;
			} catch (Exception ex) {
				log.warn("Something went wrong getting Employees");
				return null;
			}
	}
	
	@Override
	public void updateEmployee(Employee e) {
		String query = "UPDATE employees SET employeeEmail = ?, name = ?, "
				+ "supervisor = ?, deptHead = ?, role = ?, totalReimbursements = ?, "
				+ "pendingReimbursements = ?, awardedReimbursements = ?, availableReimbursements = ? WHERE employeeId = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(e.getEmployeeEmail(),
				e.getName(),
				e.getSupervisor(),
				e.getDeptHead(),
				e.getRole(),
				e.getTotalReimbursements(),
				e.getPendingReimbursements(),
				e.getAwardedReimbursements(),
				e.getAvailableReimbursements(),
				e.getEmployeeId()
				);
		session.execute(bound);
	}
	

	@Override
	public void updateRequests() {
		List<Employee> employees = getEmployees();
		for(Employee e : employees) {
			float pending = 0f;
			float awarded = 0f;
			List<Request> requests = rdao.getRequestsByEmployee(e);
			for(Request r : requests) {
				float multi = 0f;
				switch(r.getType()) {
				case CERTIFICATION: multi = 1.00f;
				break;
				case CERT_PREP_CLASS: multi = 0.75f;
				break;
				case SEMINAR: multi = 0.60f;
				break;
				case TECHNICAL_TRAINING: multi = 0.90f;
				break;
				case UNICOURSE: multi = 0.80f;
				break;
				case OTHER: multi = 0.30f;
				break;
				default: break;
				}
				if(r.getStatus().equals(Status.PENDING)) {
					r.setProjectedAward(r.getEventCost()*multi);
					pending += r.getProjectedAward();
				}
				if(r.getStatus().equals(Status.AWARDED)) {
					r.setProjectedAward(r.getEventCost()*multi);
					awarded += r.getAwardValue();
				}
			}
			e.setPendingReimbursements(pending);
			e.setAwardedReimbursements(awarded);
			e.setAvailableReimbursements(e.getTotalReimbursements() - e.getPendingReimbursements() - e.getAwardedReimbursements());
		}
	}
}
