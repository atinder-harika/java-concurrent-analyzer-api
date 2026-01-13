package com.analyzer.controller;

import com.analyzer.model.AnalysisRequest;
import com.analyzer.model.AnalysisResponse;
import com.analyzer.service.ConcurrentAnalysisService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private final ConcurrentAnalysisService analysisService;
    
    public AnalysisController(ConcurrentAnalysisService analysisService) {
        this.analysisService = analysisService;
    }
    
    @PostMapping("/analyze")
    public CompletableFuture<ResponseEntity<AnalysisResponse>> analyzeCode(
            @Valid @RequestBody AnalysisRequest request) {
        
        logger.info("Received analysis request for repository: {}", request.getRepositoryUrl());
        
        return analysisService.analyzeRepository(request)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Analysis failed", ex);
                    return ResponseEntity.internalServerError().build();
                });
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is healthy");
    }
}
