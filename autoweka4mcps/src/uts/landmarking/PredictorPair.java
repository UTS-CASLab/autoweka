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
public class PredictorPair {
    String predictorName;
    Integer counter;

    public PredictorPair() {
    }

    public PredictorPair(String predictorName, Integer counter) {
        this.predictorName = predictorName;
        this.counter = counter;
    }

    public String getPredictorName() {
        return predictorName;
    }

    public void setPredictorName(String predictorName) {
        this.predictorName = predictorName;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
 
    
    
    
    
    
}
