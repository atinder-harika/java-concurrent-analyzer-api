package com.analyzer.controller;

import com.analyzer.model.AnalysisRequest;
import com.analyzer.service.ConcurrentAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalysisController.class)
class AnalysisControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ConcurrentAnalysisService analysisService;
    
    @Test
    void healthEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is healthy"));
    }
    
    @Test
    void analyzeEndpointAcceptsValidRequest() throws Exception {
        String requestBody = """
                {
                    "repositoryUrl": "https://github.com/user/repo",
                    "branch": "main",
                    "concurrencyLevel": 4
                }
                """;
        
        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }
}
