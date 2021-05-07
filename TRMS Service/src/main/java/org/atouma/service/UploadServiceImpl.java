package org.atouma.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Request;
import org.atouma.beans.Upload;
import org.atouma.data.UploadDao;
import org.atouma.data.UploadDaoImpl;
import org.atouma.factory.BeanFactory;
import org.atouma.utils.S3Util;

public class UploadServiceImpl implements UploadService {
	
	private static final Logger log = LogManager.getLogger(UploadServiceImpl.class);
	private static RequestService rs = (RequestService) BeanFactory.getFactory().get(RequestService.class, 
			RequestServiceImpl.class);
	private static EmployeeService empServ = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, 
			EmployeeServiceImpl.class);
	private static S3Util s3util = S3Util.getInstance();
	
	
	UploadDao uploadDao;

	public UploadServiceImpl() {
		this.uploadDao = new UploadDaoImpl();
	}

	public UploadServiceImpl(UploadDao uploadDao) {
		this.uploadDao = uploadDao;
	}

	public List<Upload> getUploads(Employee e) {
		try {
			return uploadDao.getUploads(e);
		} catch(Exception ex) {
			log.warn(ex);
			for(StackTraceElement emp : ex.getStackTrace()) {
				log.debug(emp);
			}
			return null;
		}		
	}
	
	public boolean addUpload(Upload upload) {
		log.trace("Uservice adding upload.");
		try {
			uploadDao.addUpload(upload);
			return true;
		} catch (Exception ex) {
			log.warn(ex);
			for(StackTraceElement e : ex.getStackTrace()) {
				log.debug(e);
			}
			return false;
		}
	}
	
	public boolean deleteUpload(Upload upload) {
		log.trace("UService delete upload.");
		try {
			uploadDao.deleteUpload(upload);
			return true;
		} catch (Exception ex) {
			log.warn(ex);
			for (StackTraceElement e : ex.getStackTrace()) {
				log.debug(e);
			}
			return false;
		}
	}
	public void setUploadDao(UploadDao dao) {
		this.uploadDao = dao;
	}

	public void getRequestsAttachment(Employee e, Integer requestId, String filename) {
		Request r = rs.getRequestById(requestId);
		Employee applicant = empServ.getEmployeeById(r.getEmployeeId());
		if(e.getEmployeeId() == applicant.getEmployeeId() 
				|| e.getEmployeeId() == applicant.getSupervisor()
				|| e.getEmployeeId() == applicant.getDeptHead()
				|| e.getEmployeeId() == r.getAssignedBenCoId()) {
			s3util.getObject(filename);
				
		}
		
	}

}
