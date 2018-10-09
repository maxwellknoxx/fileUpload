package com.maxwell.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maxwell.blog.model.DBFile;

@Repository
public interface DBFileRepository  extends JpaRepository<DBFile, String>{
	
	DBFile findByFileName(String fileName);

}
