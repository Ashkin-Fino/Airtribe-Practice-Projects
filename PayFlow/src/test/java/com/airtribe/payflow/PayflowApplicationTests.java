package com.airtribe.payflow;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.repository.UserRepository;

@SpringBootTest
class PayflowApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testQuery() {
		Optional<User> user = userRepository.findByUpiId("ashkin@upi");
		try {
			System.out.println(user.get().toString());
		} catch (NoSuchElementException e) {
			System.out.println("User not found");
		}
	}
}
