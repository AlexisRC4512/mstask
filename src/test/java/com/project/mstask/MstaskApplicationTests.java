package com.project.mstask;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MstaskApplicationTests {


	@Test
	void contextLoads() {
		assertNotNull("El contexto no es nulo");
	}

}
