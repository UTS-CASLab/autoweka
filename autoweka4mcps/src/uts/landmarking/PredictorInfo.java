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
public class PredictorInfo {
    
    String predictorName;
    String predictorShortName;
    String pipelineStructure;
    Double min10foldcvError;

    public PredictorInfo() {
    }

    public PredictorInfo(String predictorName, String predictorShortName, String pipelineStructure, Double min10foldcvError) {
        this.predictorName = predictorName;
        this.predictorShortName = predictorShortName;
        this.pipelineStructure = pipelineStructure;
        this.min10foldcvError = min10foldcvError;
    }

    public String getPredictorName() {
        return predictorName;
    }

    public void setPredictorName(String predictorName) {
        this.predictorName = predictorName;
    }

    public String getPredictorShortName() {
        return predictorShortName;
    }

    public void setPredictorShortName(String predictorShortName) {
        this.predictorShortName = predictorShortName;
    }

    public String getPipelineStructure() {
        return pipelineStructure;
    }

    public void setPipelineStructure(String pipelineStructure) {
        this.pipelineStructure = pipelineStructure;
    }

    public Double getMin10foldcvError() {
        return min10foldcvError;
    }

    public void setMin10foldcvError(Double min10foldcvError) {
        this.min10foldcvError = min10foldcvError;
    }

    
    
    
    
}
