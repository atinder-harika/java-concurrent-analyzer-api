package com.analyzer.service;

import com.analyzer.model.AnalysisRequest;
import com.analyzer.model.AnalysisResponse;
import com.analyzer.model.CodeMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ConcurrentAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentAnalysisService.class);
    private final ExecutorService executorService;
    
    public ConcurrentAnalysisService() {
        int cores = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(cores);
        logger.info("Initialized thread pool with {} threads", cores);
    }
    
    public CompletableFuture<AnalysisResponse> analyzeRepository(AnalysisRequest request) {
        long startTime = System.currentTimeMillis();
        logger.info("Starting concurrent analysis with {} threads", request.getConcurrencyLevel());
        
        return CompletableFuture.supplyAsync(() -> {
            List<String> files = simulateFileDiscovery(request.getRepositoryUrl());
            
            List<CompletableFuture<CodeMetrics>> analysisTasks = files.stream()
                    .map(file -> CompletableFuture.supplyAsync(
                            () -> analyzeFile(file), executorService))
                    .collect(Collectors.toList());
            
            CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                    analysisTasks.toArray(new CompletableFuture[0]));
            
            return allTasks.thenApply(v -> {
                List<CodeMetrics> results = analysisTasks.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
                
                return aggregateResults(results, startTime);
            }).join();
        }, executorService);
    }
    
    private CodeMetrics analyzeFile(String filePath) {
        logger.debug("Analyzing file: {} on thread: {}", 
                filePath, Thread.currentThread().getName());
        
        simulateProcessing();
        
        return CodeMetrics.builder()
                .fileName(filePath)
                .linesOfCode(new Random().nextInt(500) + 50)
                .cyclomaticComplexity(new Random().nextInt(20) + 1)
                .methodCount(new Random().nextInt(30) + 1)
                .build();
    }
    
    private List<String> simulateFileDiscovery(String repositoryUrl) {
        int fileCount = new Random().nextInt(100) + 50;
        List<String> files = new ArrayList<>();
        for (int i = 0; i < fileCount; i++) {
            files.add("File_" + i + ".java");
        }
        logger.info("Discovered {} files for analysis", fileCount);
        return files;
    }
    
    private void simulateProcessing() {
        try {
            Thread.sleep(new Random().nextInt(50) + 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Analysis interrupted", e);
        }
    }
    
    private AnalysisResponse aggregateResults(List<CodeMetrics> metrics, long startTime) {
        int totalFiles = metrics.size();
        int totalLoc = metrics.stream().mapToInt(CodeMetrics::getLinesOfCode).sum();
        int totalComplexity = metrics.stream().mapToInt(CodeMetrics::getCyclomaticComplexity).sum();
        double avgMethodLength = metrics.stream()
                .mapToDouble(m -> (double) m.getLinesOfCode() / m.getMethodCount())
                .average()
                .orElse(0.0);
        
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Analysis completed in {}ms", executionTime);
        
        return AnalysisResponse.builder()
                .status("completed")
                .totalFiles(totalFiles)
                .linesOfCode(totalLoc)
                .cyclomaticComplexity(totalComplexity)
                .averageMethodLength(avgMethodLength)
                .patterns(Arrays.asList("singleton", "factory", "strategy"))
                .issues(Collections.emptyList())
                .executionTimeMs(executionTime)
                .threadsUsed(Runtime.getRuntime().availableProcessors())
                .build();
    }
}
