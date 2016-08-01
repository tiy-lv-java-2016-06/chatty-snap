package com.theironyard;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import com.theironyard.services.PhotoRepository;
import com.theironyard.services.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IronGramApplication.class)
@WebAppConfiguration
public class IronGramApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PhotoRepository photoRepository;

	@Autowired
	WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
	public void before(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	}

	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login").param("username", "TestName").param("password", "TestPassword")
		).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

		assertTrue(userRepository.count() == 1);
	}

	@Test
	public void testLogout() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/logout")
		).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public void testGetUser() throws Exception {
		User user = new User("Bill", "password");
		userRepository.save(user);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/user").requestAttr("Bill", user)
		).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		assertNotNull(user);
	}

	@Test
	public void testUpload(){

	}

	@Test
	public void	testGetPhotos() throws Exception {
		User sender = new User("Sam", "test");
		User recipient = new User("Ray", "test2");
		Photo photo = new Photo(sender,recipient,"testName");
		userRepository.save(sender);
		userRepository.save(recipient);
		photoRepository.save(photo);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/photos").sessionAttr("Ray", recipient).requestAttr("photo", photo)
		);

		assertNotNull(photo);
	}

	@Test
	public void testGetPublicPhotos(){
		
	}

}

