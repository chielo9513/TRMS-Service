package org.atouma.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atouma.beans.Employee;
import org.atouma.beans.Upload;
import org.atouma.service.UploadService;
import org.atouma.service.UploadServiceImpl;
import org.atouma.utils.S3Util;

import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.core.sync.RequestBody;

public class UploadController {
private static UploadService service = new UploadServiceImpl();
	
	private static Logger logger = LogManager.getLogger(UploadController.class);

	
	
	// authenticated
	// POST /uploads 
	public static void uploadFile(Context ctx){
		logger.trace("Uploading files");
		
		List<Upload> uploads = new ArrayList<>();
		Employee employee = ctx.sessionAttribute("Employee");
		Random r = new Random();
		
		for(UploadedFile file : ctx.uploadedFiles()) {	
			
			//generate a key and upload to s3
			String key = employee.getEmployeeId() + "." + r.nextInt(Integer.MAX_VALUE) + file.getFilename();
			
			S3Util.getInstance().uploadToBucket(key, 
					RequestBody.fromInputStream(file.getContent(), file.getSize()));
			String url = S3Util.getInstance().getObjectUrl(key);
			
			//create upload object, add it to db, and add it to list
			Upload upload = new Upload();
			upload.setEmployeeID(employee.getEmployeeId());
			upload.setS3Key(key);
			upload.setFileURL(url);
			
			service.addUpload(upload);
			
			uploads.add(upload);
		}
		
		ctx.json(uploads);
	}
	
	// authenticated
	// GET /uploads
	public void getFilesByUser(Context ctx) {
		logger.trace("Get files by Employee");
		
		Employee user = ctx.sessionAttribute("Employee");
		List<Upload> files = ((UploadServiceImpl) service).getUploads(user);
		ctx.json(files);
		
	}
	
	public void getRequestsAttachments(Context ctx) {
		Integer requestId = ctx.formParam("requestId", Integer.class).get();
		Employee e = ctx.sessionAttribute("Employee");
		String filename = ctx.formParam("filename", String.class).get();
		service.getRequestsAttachment(e, requestId, filename);
	}

	// authenticated
	// DELETE /uploads/:id
	public void deleteFile(Context ctx) {
		logger.trace("Delete File " + ctx.pathParam("s3Key"));
		
		Employee employee = ctx.sessionAttribute("Employee");
		String s3Key = ctx.pathParam("s3Key");
		S3Util.getInstance().deleteObject(s3Key);
		
		Upload upload = new Upload();
		upload.setEmployeeID(employee.getEmployeeId());
		upload.setFileURL(S3Util.getInstance().getObjectUrl(s3Key));
		upload.setS3Key(s3Key);
		
		if(((UploadServiceImpl) service).deleteUpload(upload)) {
			ctx.status(204);
		}else {
			throw new InternalServerErrorResponse();
		}
	}

}
