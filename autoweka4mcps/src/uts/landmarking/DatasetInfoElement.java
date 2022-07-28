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
public class DatasetInfoElement {
    
    private String dataset;
    private String predictor;
    private Double automl_error;
    private Double default_error;
    private Double automl_rank;
    private Double default_rank;
    private Double automl_std;
    private Double default_std;
    private Integer automl_n_evaluations;
    private Integer default_n_evaluations;
    private String evaluationTime;

    public DatasetInfoElement() {
    }

    public DatasetInfoElement(String dataset, String predictor) {
        this.dataset = dataset;
        this.predictor = predictor;
    }
    
    

    public DatasetInfoElement(String dataset, String predictor, Double automl_error, Double default_error, Double automl_rank, Double default_rank) {
        this.dataset = dataset;
        this.predictor = predictor;
        this.automl_error = automl_error;
        this.default_error = default_error;
        this.automl_rank = automl_rank;
        this.default_rank = default_rank;
    }

    public DatasetInfoElement(String dataset, String predictor, Double automl_error, Double automl_rank, Double automl_std, Integer automl_n_evaluations) {
        this.dataset = dataset;
        this.predictor = predictor;
        this.automl_error = automl_error;
        this.automl_rank = automl_rank;
        this.automl_std = automl_std;
        this.automl_n_evaluations = automl_n_evaluations;
    }

    
    
    

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getPredictor() {
        return predictor;
    }

    public void setPredictor(String predictor) {
        this.predictor = predictor;
    }

    public Double getAutoml_error() {
        return automl_error;
    }

    public void setAutoml_error(Double automl_error) {
        this.automl_error = automl_error;
    }

    public Double getDefault_error() {
        return default_error;
    }

    public void setDefault_error(Double default_error) {
        this.default_error = default_error;
    }

    public Double getAutoml_rank() {
        return automl_rank;
    }

    public void setAutoml_rank(Double automl_rank) {
        this.automl_rank = automl_rank;
    }

    public Double getDefault_rank() {
        return default_rank;
    }

    public void setDefault_rank(Double default_rank) {
        this.default_rank = default_rank;
    }

    public Double getAutoml_std() {
        return automl_std;
    }

    public void setAutoml_std(Double automl_std) {
        this.automl_std = automl_std;
    }

    public Double getDefault_std() {
        return default_std;
    }

    public void setDefault_std(Double default_std) {
        this.default_std = default_std;
    }

    public Integer getAutoml_n_evaluations() {
        return automl_n_evaluations;
    }

    public void setAutoml_n_evaluations(Integer automl_n_evaluations) {
        this.automl_n_evaluations = automl_n_evaluations;
    }

    public Integer getDefault_n_evaluations() {
        return default_n_evaluations;
    }

    public void setDefault_n_evaluations(Integer default_n_evaluations) {
        this.default_n_evaluations = default_n_evaluations;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    
    
    
    
}
