package com.maxwell.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.maxwell.blog.model.DBFile;
import com.maxwell.blog.service.DBFileStorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
@AutoConfigureMockMvc(secure = false)
public class UploadControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	DBFile file;
	DBFile file1;
	List<DBFile> list;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		list = new ArrayList<>();
		file = new DBFile();
		byte[] data = {1, 2};
		file.setId("171-cak-5a41");
		file.setFileName("The World.pdf");
		file.setFileType("application/pdf");
		file.setUploadDate("10/10/2018");
		file.setData(data);
		
		file1 = new DBFile();
		byte[] data1 = {3, 4};
		file1.setId("123-kac-41a5");
		file1.setFileName("The Earth.pdf");
		file1.setFileType("application/pdf");
		file1.setUploadDate("11/10/2018");
		file1.setData(data1);
		
		list.add(file);
		list.add(file1);
	}

	@MockBean
	private DBFileStorageService service;

	@Test
	public void shouldReturnSuccessFromService() throws Exception {
		assertThat(this.service).isNotNull();
		mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8")).andExpect(view().name("/form"))
				.andExpect(content().string(Matchers.containsString("Upload Form"))).andDo(print());
	}

	@Test
	public void testGetFile() throws Exception {
		assertThat(this.service).isNotNull();
		when(service.getFile("171-cak-5a41")).thenReturn(file);
		MvcResult result = mockMvc.perform(get("/downloadFile/{fileId}", "171-cak-5a41")).andExpect(status().isOk())
				.andReturn();

		MockHttpServletResponse mockResponse = result.getResponse();
		assertThat(mockResponse.getContentType()).isEqualTo("application/pdf");

		Collection<String> responseHeaders = mockResponse.getHeaderNames();
		assertNotNull(responseHeaders);
	}
	
	@Test
	public void testFindAll() {
		assertThat(this.service).isNotNull();
		when(service.findAll()).thenReturn(list);
	}

}
