package com.analyzer.model;

import java.util.List;

public class AnalysisResponse {
    private String status;
    private int totalFiles;
    private int linesOfCode;
    private int cyclomaticComplexity;
    private double averageMethodLength;
    private List<String> patterns;
    private List<String> issues;
    private long executionTimeMs;
    private int threadsUsed;
    
    public AnalysisResponse() {
    }
    
    public AnalysisResponse(String status, int totalFiles, int linesOfCode, 
                           int cyclomaticComplexity, double averageMethodLength,
                           List<String> patterns, List<String> issues, 
                           long executionTimeMs, int threadsUsed) {
        this.status = status;
        this.totalFiles = totalFiles;
        this.linesOfCode = linesOfCode;
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.averageMethodLength = averageMethodLength;
        this.patterns = patterns;
        this.issues = issues;
        this.executionTimeMs = executionTimeMs;
        this.threadsUsed = threadsUsed;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getTotalFiles() {
        return totalFiles;
    }
    
    public void setTotalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
    }
    
    public int getLinesOfCode() {
        return linesOfCode;
    }
    
    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }
    
    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }
    
    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }
    
    public double getAverageMethodLength() {
        return averageMethodLength;
    }
    
    public void setAverageMethodLength(double averageMethodLength) {
        this.averageMethodLength = averageMethodLength;
    }
    
    public List<String> getPatterns() {
        return patterns;
    }
    
    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }
    
    public List<String> getIssues() {
        return issues;
    }
    
    public void setIssues(List<String> issues) {
        this.issues = issues;
    }
    
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    
    public int getThreadsUsed() {
        return threadsUsed;
    }
    
    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }
    
    public static class Builder {
        private String status;
        private int totalFiles;
        private int linesOfCode;
        private int cyclomaticComplexity;
        private double averageMethodLength;
        private List<String> patterns;
        private List<String> issues;
        private long executionTimeMs;
        private int threadsUsed;
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder totalFiles(int totalFiles) {
            this.totalFiles = totalFiles;
            return this;
        }
        
        public Builder linesOfCode(int linesOfCode) {
            this.linesOfCode = linesOfCode;
            return this;
        }
        
        public Builder cyclomaticComplexity(int cyclomaticComplexity) {
            this.cyclomaticComplexity = cyclomaticComplexity;
            return this;
        }
        
        public Builder averageMethodLength(double averageMethodLength) {
            this.averageMethodLength = averageMethodLength;
            return this;
        }
        
        public Builder patterns(List<String> patterns) {
            this.patterns = patterns;
            return this;
        }
        
        public Builder issues(List<String> issues) {
            this.issues = issues;
            return this;
        }
        
        public Builder executionTimeMs(long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }
        
        public Builder threadsUsed(int threadsUsed) {
            this.threadsUsed = threadsUsed;
            return this;
        }
        
        public AnalysisResponse build() {
            return new AnalysisResponse(status, totalFiles, linesOfCode, 
                    cyclomaticComplexity, averageMethodLength, patterns, 
                    issues, executionTimeMs, threadsUsed);
        }
    }
}
