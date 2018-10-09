package com.maxwell.blog.payload;

public class UploadFileResponse {

	private String fileName;
	private String downloadLink;
	private String fileType;
	private String uploadDate;

	public UploadFileResponse(String fileName, String downloadLink, String fileType, String uploadDate) {
		this.fileName = fileName;
		this.downloadLink = downloadLink;
		this.fileType = fileType;
		this.uploadDate = uploadDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

}
