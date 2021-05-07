package org.atouma.service;

import java.util.List;

import org.atouma.beans.Employee;
import org.atouma.beans.Upload;

public interface UploadService {

	List<Upload> getUploads(Employee e);
	
	boolean addUpload(Upload upload);

	void getRequestsAttachment(Employee e, Integer requestId, String filename);

	boolean deleteUpload(Upload upload);
}