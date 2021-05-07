//package org.atouma.ServiceTests;
//
//import static org.mockito.ArgumentMatchers.anyInt;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.atouma.beans.Employee;
//import org.atouma.data.EmployeeDao;
//import org.atouma.service.EmployeeService;
//import org.atouma.service.EmployeeServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//public class EmployeeServiceTest {
//	@Mock
//	private EmployeeDao edao = null;
//	
//	private EmployeeService eServ = null;
//	
//	@BeforeEach
//	private void setup() {
//		Employee e = new Employee();
//		Employee i = new Employee();
//		Employee k = new Employee();
//		
//		e.setEmployeeEmail("a@b.com");
//		e.setEmployeeFirstName("TJ");
//		e.setEmployeeLastName("Touma"); 
//		e.setEmployeeId(1);
//		e.setDepartment(1);
//		e.setDirectSuper(true);
//		e.setDeptHead(true);
//		e.setBenCo(true);
//		e.setDirectSuperId(2); 
//		e.setDeptHeadId(3);
//		e.setTotalReimbursements(1000f);
//		e.setPendingReimbursements(500f);
//		e.setAwardedReimbursements(500f);
//		
//		i.setEmployeeEmail("s@b.com");
//		i.setEmployeeFirstName("Steph");
//		i.setEmployeeLastName("Tallman"); 
//		i.setEmployeeId(2);
//		i.setDepartment(2);
//		i.setDirectSuper(true);
//		i.setDeptHead(true);
//		i.setBenCo(true);
//		i.setDirectSuperId(4); 
//		i.setDeptHeadId(5);
//		i.setTotalReimbursements(1000f);
//		i.setPendingReimbursements(500f);
//		i.setAwardedReimbursements(500f);
//		
//		k.setEmployeeEmail("r@b.com");
//		k.setEmployeeFirstName("Richard");
//		k.setEmployeeLastName("Orr");
//		k.setEmployeeId(3);
//		k.setDepartment(1);
//		k.setDirectSuper(false);
//		k.setDeptHead(false);
//		k.setBenCo(false);
//		k.setDirectSuperId(6); 
//		k.setDeptHeadId(7);
//		k.setTotalReimbursements(1000f);
//		k.setPendingReimbursements(500f);
//		k.setAwardedReimbursements(500f);
//		
//		Set<Employee> set = new HashSet<>();
//		set.add(e);
//		set.add(i);
//		set.add(k);
//		
//		Mockito.when(edao.getEmployeeById(anyInt())).then(j->{
//			int id = (int) j.getArguments()[0];
//			if(id == 1) {
//				return e;
//			} if(id == 2) {
//				return i;
//			} if(id == 3) {
//				return k;
//			}
//			return null;
//		});
//		
//		Mockito.when(edao.addEmployee(Mockito.any(Employee.class))).then(j->{
//			Employee employee = (Employee) j.getArguments()[0];
//			set.add(employee);
//			System.out.println("created new employee "+employee);
//			return employee;
//		});
//		
//		this.eServ = new EmployeeServiceImpl(edao);
//	}
//	
//	@Test
//	private void getEmployeeTest() {
//		Employee x = eServ.getEmployeeById(1);
//		Assertions.assertEquals("TJ", x.getEmployeeFirstName());
//	}
//	@Test
//	private void getEmployeeTest2() {
//		Employee y = eServ.getEmployeeById(5);
//		Assertions.assertNull(y);
//	}
//	@Test
//	private void getEmployeeTest3() {
//		String[] names = {"TJ", "Steph", "Richard"};
//		for(int i=0;i<3;i++) {
//			Employee x = eServ.getEmployeeById(i+1);
//			Assertions.assertEquals(names[i], x.getEmployeeFirstName());
//		}
//	}
//	
//	
//}
