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
public class OneConfiguration {
    private Double estimatedError;

    private String bestPipeline;

    public OneConfiguration() {
    }

    public OneConfiguration(Double estimatedError, String bestPipeline) {
        this.estimatedError = estimatedError;
        this.bestPipeline = bestPipeline;
    }

    public Double getEstimatedError() {
        return estimatedError;
    }

    public void setEstimatedError(Double estimatedError) {
        this.estimatedError = estimatedError;
    }

    public String getBestPipeline() {
        return bestPipeline;
    }

    public void setBestPipeline(String bestPipeline) {
        this.bestPipeline = bestPipeline;
    }

    
    
    
    
}
