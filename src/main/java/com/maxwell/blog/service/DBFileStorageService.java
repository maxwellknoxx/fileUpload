package com.maxwell.blog.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.maxwell.blog.exception.FileStorageException;
import com.maxwell.blog.exception.MyFileNotFoundException;
import com.maxwell.blog.model.DBFile;
import com.maxwell.blog.repository.DBFileRepository;

@Service
public class DBFileStorageService {

	@Autowired
	private DBFileRepository dbFileRepository;

	public DBFile storeFile(MultipartFile file, String dateHour) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			DBFile dbFile = new DBFile(fileName, file.getContentType(), dateHour, "", file.getBytes());

			if (fileExist(fileName)) {
				return null;
			}

			return dbFileRepository.save(dbFile);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}
	
	public boolean updateDownloadLink(DBFile dbFile) {
		if(dbFileRepository.save(dbFile) != null) {
			return true;
		}
		return false;
	}

	public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
	
	public List<DBFile> findAll(){
		return dbFileRepository.findAll();
	}

	public boolean fileExist(String fileName) {
		DBFile file = dbFileRepository.findByFileName(fileName);
		if (file != null) {
			return true;
		}
		return false;
	}

}
