package com.park.monitoring.controller;

import com.park.monitoring.service.DiskService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ServerInfoController.class)
public class DiskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiskService diskService;
}
