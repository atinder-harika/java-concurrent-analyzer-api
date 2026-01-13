package com.analyzer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AnalysisRequest {
    
    @NotBlank(message = "Repository URL is required")
    private String repositoryUrl;
    
    private String branch = "main";
    
    @Min(value = 1, message = "Concurrency level must be at least 1")
    private int concurrencyLevel = 4;
    
    public AnalysisRequest() {
    }
    
    public AnalysisRequest(String repositoryUrl, String branch, int concurrencyLevel) {
        this.repositoryUrl = repositoryUrl;
        this.branch = branch;
        this.concurrencyLevel = concurrencyLevel;
    }
    
    public String getRepositoryUrl() {
        return repositoryUrl;
    }
    
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public int getConcurrencyLevel() {
        return concurrencyLevel;
    }
    
    public void setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }
}

