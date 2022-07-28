/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.landmarking;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntdun
 */
public class ConfigurationExp {

    private String uniqueConfiguration;
    private List<OneConfiguration> listOfOneConfigurations;
      

    public ConfigurationExp() {
    }

    public String getUniqueConfiguration() {
        return uniqueConfiguration;
    }

    public void setUniqueConfiguration(String uniqueConfiguration) {
        this.uniqueConfiguration = uniqueConfiguration;
    }

    public List<OneConfiguration> getListOfOneConfigurations() {
        return listOfOneConfigurations;
    }

    public void setListOfOneConfigurations(List<OneConfiguration> listOfOneConfigurations) {
        this.listOfOneConfigurations = listOfOneConfigurations;
    }

    
    

    
}
