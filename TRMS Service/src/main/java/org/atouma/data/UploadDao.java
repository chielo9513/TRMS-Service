package org.atouma.data;

import java.util.List;

import org.atouma.beans.Employee;
import org.atouma.beans.Upload;

public interface UploadDao {

	List<Upload> getUploads(Employee employee);

	void addUpload(Upload upload);

	void deleteUpload(Upload upload);

}