package com.theironyard;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import com.theironyard.services.PhotoRepository;
import com.theironyard.services.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IronGramApplication.class)
@WebAppConfiguration
public class IronGramApplicationTests {

	@Autowired
	WebApplicationContext wap;

	@Autowired
	UserRepository users;

	@Autowired
	PhotoRepository photos;

	MockMvc mockMvc;

	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	}

	@Test
	public void testLogin() throws Exception {
		String TestName = "nigel";
		String Testpassword = "hello";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
				.param("username", "TestName")
				.param("password", "TestPassword")
		).andExpect(status().is3xxRedirection());

		assertTrue(users.count() >= 1);
	}

	@Test
	public void testLogout() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/logout")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	public void testUser() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user")
		);
	}

	@Test
	public void testUpload() throws Exception {
		long time;
		mockMvc.perform(
				MockMvcRequestBuilders.post("/upload")
					.param("photo", "file")
					.param("receiverName", "nigel")
					.param("fileDuration", "time")
					.param("makePrivate", "p")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	public void testPhotos() throws Exception {
		String name = "me";
		User user = users.findFirstByName(name);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/photos")
		);
	}

	@Test
	public void testPublicPhotos() throws Exception {
		String name = "me";
		User user = users.findFirstByName(name);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/public-photos")
		);
	}

	@Test
	public void testDelete() throws Exception {
		String name = "me";
		User user = users.findFirstByName(name);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/delete")
		);
	}

}
