package com.maxwell.blog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.maxwell.blog.model.DBFile;
import com.maxwell.blog.service.DBFileStorageService;

@Controller
public class UploadController {

	@Autowired
	private DBFileStorageService dbFileStorageService;

	/**
	 * Redirect to index page.
	 * 
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView pageForm() {
		ModelAndView mv = new ModelAndView("/form");
		mv.addObject("files", dbFileStorageService.findAll());
		return mv;
	}

	/**
	 * Allow to upload a single file.
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping("/")
	public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) {
		ModelAndView mv = new ModelAndView("/form");
		String dateAndHour = getCurrentDateAndHour();
		DBFile dbFile = dbFileStorageService.storeFile(file, dateAndHour);

		if (dbFile != null) {
			String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(dbFile.getId()).toUriString();
			dbFile.setDownloadLink(downloadLink);
			dbFileStorageService.updateDownloadLink(dbFile);
			mv.addObject("files", dbFileStorageService.findAll());
		} else {
			mv.addObject("message", "File with name " + file.getOriginalFilename() + " already exist!");
			mv.addObject("files", dbFileStorageService.findAll());
		}
		
		return mv;
	}

	/**
	 * Download the PDF from database
	 * 
	 * @param fileId
	 * @return PDF as a resource
	 */
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
		// Load file from database
		DBFile dbFile = dbFileStorageService.getFile(fileId);

		if (dbFile == null) {
			return null;
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}

	/**
	 * 
	 * @return Current date and hour
	 */
	public String getCurrentDateAndHour() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

}
