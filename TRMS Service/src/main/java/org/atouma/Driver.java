package org.atouma;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.controllers.EmployeeController;
import org.atouma.controllers.MessageController;
import org.atouma.controllers.RequestController;
import org.atouma.controllers.UploadController;
//import org.atouma.data.EmployeeDao;
//import org.atouma.data.EmployeeDaoImpl;
//import org.atouma.data.RequestDao;
//import org.atouma.data.RequestDaoImpl;
//import org.atouma.factory.BeanFactory;
import org.atouma.utils.CassandraUtil;

//import com.datastax.oss.driver.api.core.CqlSession;
//import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

import io.javalin.Javalin;

public class Driver {
	
	/*
	 *  TODO
	 *  
	 */
	
	
	public static final int PORT = 8080;
	private static final Logger log = LogManager.getLogger(Driver.class);
	//private static RequestDao rdao = (RequestDao) BeanFactory.getFactory().get(RequestDao.class, RequestDaoImpl.class);
	//private static EmployeeDao edao = (EmployeeDao) BeanFactory.getFactory().get(EmployeeDao.class, EmployeeDaoImpl.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.trace("Begin Tuition Reimbursement Management System application");
		javalin();
		//dbtest();
		messagesTable();
		requestTable();
		uploadTable();
		employeesTable();
		
		//rdao.timeoutRequests();
		//edao.updateRequests();
		
	}
	

	private static void javalin() {
		Javalin app = Javalin.create(conf -> {
			conf.requestLogger((ctx, responseTime) -> {
				log.debug(ctx.method() + " ->" +ctx.path() + " -> " + responseTime + "ms");
			});
			conf.enableDevLogging();
		}).start(PORT);
		
		app.get("/employees/:employeeId/requests", RequestController::viewAllRequests);
		app.put("/employee", EmployeeController::addEmployee);
		app.post("/employees", EmployeeController::login);
		app.delete("/empLogout", EmployeeController::logout);
		
		//GETTING ATTACHMENTS app.get()
		app.post("/uploads", UploadController::uploadFile);
		//delete attachments?
		
		app.post("/employee/:employeeId", RequestController::submitRequest);
		//app.delete("/employees/:employeeId/requests/:requestId", RequestController::cancelRequest);
		
		app.post("/requests", RequestController::approveRequest);
		app.get("/requests", RequestController::viewRequestById);
		
		app.post("/employees/:employeeId/messages", MessageController::sendMessage);
	
		CassandraUtil.getInstance();
	}
	
	public static void uploadTable() {
		
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("uploads (")
				.append("employeeId int,")
				.append("s3Key text,")
				.append("fileURL text,")
				.append("PRIMARY KEY (employeeId, s3Key));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		log.trace("Driver creating uploadsTable");
	}
	
	public static void requestTable(){
		
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("requests (") //table name
				.append("requestId int PRIMARY KEY,")
				.append("employeeId int,")
				.append("assignedBenCoId int,")
				.append("eventLocation text,")
				.append("eventDescription text,")
				.append("eventTimeZone text,")
				.append("eventDateTime timestamp,")
				.append("eventCost float,")
				.append("eventGradingFormat text,")
				.append("reason text,")
				//enums?
				.append("status text,")
				.append("type text,")
				//no attachments in this table
				.append("passProof boolean,")
				.append("applicationTimeZone text,")
				.append("applicationDateTime timestamp,")
				.append("isUrgent boolean,")
				.append("superApproved boolean,")
				.append("deptApproved boolean,")
				.append("benCoApproved boolean,")
				.append("projectedAward float,")
				.append("awardValue float);");
		
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		log.trace("Creating requests table if not exists");
	}
	
	public static void messagesTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("messages (")
				.append("messageId int PRIMARY KEY,")
				.append("recipientId int,")
				.append("senderId int,")
				.append("header text,")
				.append("body text);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		log.trace("Creating messages table if not exists");
	}
	
	public static void employeesTable() {
		
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("employees (") //table name
				.append("employeeId int PRIMARY KEY,")
				.append("employeeEmail text,")
				.append("name text,")
				.append("supervisor int,")
				.append("deptHead int,")
				.append("role text,")
				.append("totalReimbursements float,")
				.append("pendingReimbursements float,")
				.append("awardedReimbursements float,")
				.append("availableReimbursements float);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		log.trace("Creating employees table if not exists");
	}
	
//	public static void createRequestTable() {
//		CqlSession session = CassandraUtil.getInstance().getSession();
//		StringBuilder sb = new StringBuilder("Create table if not exists Request (")
//		//TODO NOT COMPLETED YET
//				.append("requestId int primary key, ");
//		session.execute(sb.toString());
//		
//		ObjectMapper o = new ObjectMapper();
//		Request newRequest = new Request();
//		
//		newRequest.setId(1);
//		newRequest.setValue(1);
//		newRequest.setTime(1);
//		newRequest.setStatus(1);
//		newRequest.setFlag(1);
//		newRequest.setNotes("");
//		
//		String map = null;
//		try {
//			map = o.writeValueAsString(newRequest);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public static void dbtest() {
//        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append("TRMS with replication = {")
//                .append("'class':'SimpleStrategy','replication_factor':1};");
//        DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
//        CqlSession.builder().withConfigLoader(loader).build().execute(sb.toString());
//    }

}
