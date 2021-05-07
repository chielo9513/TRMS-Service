package org.atouma.data;

import java.util.ArrayList;
import java.util.List;

import org.atouma.beans.Employee;
import org.atouma.beans.Upload;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class UploadDaoImpl implements UploadDao {
	
	private CqlSession session;
	
	private Upload buildUpload(Row row) {
		Upload upload = new Upload();
		upload.setEmployeeID(row.getInt("employeeID"));
		upload.setFileURL(row.getString("fileURL"));
		upload.setS3Key(row.getString("s3Key"));
		return upload;
	}
	
	public List<Upload> getUploads(Employee employee) {
		List<Upload> uploads = new ArrayList<>();
		String query = "select * from uploads where employeeID=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(employee.getEmployeeId());
		ResultSet results = session.execute(bound);
		
		for(Row row : results){
			uploads.add(buildUpload(row));
		}
		
		return uploads;
	}

	public void addUpload(Upload upload) {
		String query = "insert into uploads (employeeID, fileURL, s3Key) "
				+ "values (?, ?, ?)";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(upload.getEmployeeID(), upload.getFileURL(), upload.getS3Key());
		session.execute(bound);
	}

	public void deleteUpload(Upload upload) {
		String query = "delete from uploads where employeeID=? and s3Key=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(upload.getEmployeeID(), upload.getS3Key());
		session.execute(bound);
	}

}

