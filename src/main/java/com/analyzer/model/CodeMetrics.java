package com.analyzer.model;

public class CodeMetrics {
    private String fileName;
    private int linesOfCode;
    private int cyclomaticComplexity;
    private int methodCount;
    
    public CodeMetrics() {
    }
    
    public CodeMetrics(String fileName, int linesOfCode, int cyclomaticComplexity, int methodCount) {
        this.fileName = fileName;
        this.linesOfCode = linesOfCode;
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.methodCount = methodCount;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    
    public int getMethodCount() {
        return methodCount;
    }
    
    public void setMethodCount(int methodCount) {
        this.methodCount = methodCount;
    }
    
    public static class Builder {
        private String fileName;
        private int linesOfCode;
        private int cyclomaticComplexity;
        private int methodCount;
        
        public Builder fileName(String fileName) {
            this.fileName = fileName;
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
        
        public Builder methodCount(int methodCount) {
            this.methodCount = methodCount;
            return this;
        }
        
        public CodeMetrics build() {
            return new CodeMetrics(fileName, linesOfCode, cyclomaticComplexity, methodCount);
        }
    }
}
