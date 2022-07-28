/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.landmarking;

/**
 *
 * @author ntdun
 */
public class ExpResult {
    
    private Double estimatedError;
    private Integer numberOfEvaluations;
    private String bestPipeline;
    private Integer numberOfValidPipelines;

    public ExpResult() {
    }

    public ExpResult(Double estimatedError, Integer numberOfEvaluations, String bestPipeline) {
        this.estimatedError = estimatedError;
        this.numberOfEvaluations = numberOfEvaluations;
        this.bestPipeline = bestPipeline;
    }

    public Double getEstimatedError() {
        return estimatedError;
    }

    public void setEstimatedError(Double estimatedError) {
        this.estimatedError = estimatedError;
    }

    public Integer getNumberOfEvaluations() {
        return numberOfEvaluations;
    }

    public void setNumberOfEvaluations(Integer numberOfEvaluations) {
        this.numberOfEvaluations = numberOfEvaluations;
    }

    public String getBestPipeline() {
        return bestPipeline;
    }

    public void setBestPipeline(String bestPipeline) {
        this.bestPipeline = bestPipeline;
    }

    public Integer getNumberOfValidPipelines() {
        return numberOfValidPipelines;
    }

    public void setNumberOfValidPipelines(Integer numberOfValidPipelines) {
        this.numberOfValidPipelines = numberOfValidPipelines;
    }
    
    
    
    
}
