/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.landmarking;

import autoweka.Experiment;
import autoweka.TrajectoryGroup;
import autoweka.TrajectoryMerger;
import autoweka.TrajectoryParser;
import autoweka.tools.GetBestFromTrajectoryGroup;
import autoweka.utils.CSVUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ntdun
 */
public class Autoweka4MCPSExperimentAnalysis_V2 {

    private GetBestFromTrajectoryGroup mBest;
    
    private String bestPipeline;

    public ExpResult readExpFor10foldcv(String expPath, int seedNumber) {

        File expFolder = new File(expPath);

        ExpResult expResult = null;

        File modelFile = new File(expFolder.getAbsolutePath() + File.separator + "trained." + String.valueOf(seedNumber) + ".model");
        System.out.println("expFolder: " + expFolder.getAbsolutePath());
        System.out.println("modelFile: " + modelFile.getAbsolutePath());

        //if (modelFile.exists()) 
        try {

            File exp = new File(expFolder.getAbsolutePath() + File.separator + expFolder.getName() + ".experiment");

            if (!exp.exists() || !exp.isFile()) {
                throw new RuntimeException(expFolder.getAbsolutePath() + " does not appear to contain a .experiment");
            }
            TrajectoryGroup group = TrajectoryMerger.mergeExperimentFolder(expFolder.getAbsolutePath());

            ArrayList<String> inProgressSeeds = new ArrayList<String>();
            //Go through all the logs and see if we can parse any other partial trajectories
            File[] files = new File(expFolder.getAbsolutePath() + File.separator + "out" + File.separator + "logs").listFiles();
            for (File f : files) {
                String fName = f.getName();
                if (fName.endsWith(".log")) {
                    String seed = fName.substring(0, fName.length() - 4);
                    if (!group.getSeeds().contains(seed)) {
                        inProgressSeeds.add(seed);
                    }
                }
            }

            //Do we want use these inprogress seeds
            if (!inProgressSeeds.isEmpty()) {
                Collections.sort(inProgressSeeds);
                StringBuilder msgSB = new StringBuilder("It looks like there are partial runs for the following seeds:\n");
                for (String s : inProgressSeeds) {
                    msgSB.append(s);
                    msgSB.append("\n");
                }
                msgSB.append("Do you wish to use these experiments?\nYou may not be able to make predictions from\nthese experiments without training a model first.");
                //We want to use these partial runs if we're in force open mode
                if (expPath != null) {
                    for (String s : inProgressSeeds) {
                        group.addTrajectory(TrajectoryParser.getTrajectory(Experiment.createFromFolder(expFolder), expFolder, s));
                    }
                }
            }

            mBest = new GetBestFromTrajectoryGroup(group);

            String attrSelectioStr = "";
            if (mBest.attributeSearchClass != null) {

                attrSelectioStr = " - " + mBest.attributeSearchClass + " (" + mBest.attributeSearchArgs + ")";
            }
            String pipelineStr = mBest.classifierClass + " (" + mBest.classifierArgs + ")" + attrSelectioStr;
            expResult = new ExpResult((double) mBest.errorEstimate, mBest.numEval, pipelineStr);

            expResult.setNumberOfValidPipelines(getNumberOfValidEvaluations(expPath, String.valueOf(seedNumber)));

            //Set all the text blocks
            System.out.println("expFolder.getAbsolutePath(): " + expFolder.getAbsolutePath());
            System.out.println("Float.toString(mBest.errorEstimate): " + Float.toString(mBest.errorEstimate));
            System.out.println("mBest.seed: " + mBest.seed);

            System.out.println("mBest.numEval: " + (mBest.numEval == -1 ? "" : Integer.toString(mBest.numEval)));
            System.out.println("mBest.numTimeOut: " + (mBest.numTimeOut == -1 ? "" : Integer.toString(mBest.numTimeOut)));
            System.out.println("mBest.numMemOut: " + (mBest.numMemOut == -1 ? "" : Integer.toString(mBest.numMemOut)));

            System.out.println("mBest.classifierClass: " + mBest.classifierClass);
            System.out.println("mBest.classifierArgs: " + mBest.classifierArgs);
            System.out.println("mBest.attributeSearchClass: " + mBest.attributeSearchClass);
            System.out.println("mBest.attributeSearchArgs: " + mBest.attributeSearchArgs);
            System.out.println("mBest.attributeEvalClass: " + mBest.attributeEvalClass);
            System.out.println("mBest.attributeEvalArgs: " + mBest.attributeEvalArgs);

        } catch (Exception e) {
            System.out.println(e);
        }

        return expResult;

    }
    
    
    
    
    
    private ConfigurationExp isConfigurationExist(List<ConfigurationExp> listOfConfigurationExps, String id){
        
        ConfigurationExp foundConfig = null;
        
        for (ConfigurationExp configurationExp : listOfConfigurationExps) {
            
            if (configurationExp.getUniqueConfiguration().equals(id)) {
                foundConfig = configurationExp;
                break;
            }
            
        }
        
        return foundConfig;
        
    }
    
    public String getBestPipeline(){
        
        return bestPipeline;
    }
    
    

    public String readExpForSingleFold(String expPath, int seedNumber, boolean isBaseline) {

        File expFolder = new File(expPath);

        ExpResult expResult = null;
        
        List<ConfigurationExp> listOfConfigurationExps = new ArrayList<>();
        
        

        File modelFile = new File(expFolder.getAbsolutePath() + File.separator + "trained." + String.valueOf(seedNumber) + ".model");
        System.out.println("expFolder: " + expFolder.getAbsolutePath());
       

        {

            int numberOfValidPipelines = 0;
            
            String folderPath = expPath;
            
            
            String pipelinePath = folderPath + "\\avatar_log\\pipelines.txt";
            
            if (isBaseline && !pipelinePath.contains("adult")) {
                pipelinePath = folderPath + "\\avatar_log\\wrapper-pipelines.txt";
            }
            
            
            int seed = seedNumber;

            String runAndResultFilePath = folderPath + "\\out\\autoweka\\state-run" + seed;
            System.out.println("runAndResultFilePath: " + runAndResultFilePath);
            String[] pathnames;

            File f = new File(runAndResultFilePath);
            pathnames = f.list();

            if (!f.isDirectory()) {
                
                return "";
            } else {
                System.out.println("Dictectory");
                
            }

           

            String rsFileName = "";
            for (String pathname : pathnames) {
                // System.out.println(pathname);
                if (pathname.contains("runs_and_results") && pathname.contains(".csv")) {
                    rsFileName = pathname;

                    break;
                }
            }

            
            
            
            
            if (!rsFileName.equals("")) {
                // System.out.println("FOUND RESULT OUTPUT FILE !!!!! \n\n\n");
                rsFileName = runAndResultFilePath + "\\" + rsFileName;

                
                Double minQuality = Double.MAX_VALUE;
                int minQualityIndex = -1;
            
            
                
                CSVUtils cSVUtils = new CSVUtils();
                List<String[]> listOfRows = cSVUtils.readCSVFile(rsFileName);
                List<String[]> listOfRowsPipelines = cSVUtils.readCSVFile(pipelinePath);
                

                for (int i = 1; i < listOfRows.size(); i++) {
                    Double currentRunQuality = Double.parseDouble(listOfRows.get(i)[10]);

                    
                    
                    if (currentRunQuality<minQuality) {
                        minQuality = currentRunQuality;
                        minQualityIndex = i;
                    }
                    
                    
                    String runResults = listOfRows.get(i)[13].trim();

                    if (runResults.equals("SAT")) {
                        numberOfValidPipelines++;
                        
                        OneConfiguration oneConfiguration = new OneConfiguration();
                        oneConfiguration.setBestPipeline(listOfRowsPipelines.get(i-1)[0] );
                        
                        
                        oneConfiguration.setEstimatedError(currentRunQuality);
                        
                        String uniqueId = listOfRows.get(i)[1].trim();
                        
                        System.out.println("uniqueId: " + uniqueId);
                        
                        ConfigurationExp configurationExp = isConfigurationExist(listOfConfigurationExps, uniqueId);
                        
                        if (configurationExp==null) {
                            
                        
                            
                            List<OneConfiguration> listOfOneConfiguration = new ArrayList<>();
                            listOfOneConfiguration.add(oneConfiguration);
                            
                            configurationExp = new ConfigurationExp();
                            configurationExp.setListOfOneConfigurations(listOfOneConfiguration);
                            configurationExp.setUniqueConfiguration(uniqueId);
                            
                            listOfConfigurationExps.add(configurationExp);
                            
                        } else {
                            
                            configurationExp.getListOfOneConfigurations().add(oneConfiguration);
                            
                        }
                        
                        
                        

                    }

                }
                
                
                
                
                expResult = new ExpResult( minQuality, (listOfRows.size()-1), listOfRowsPipelines.get(minQualityIndex-1)[0] );
                //expResult = new ExpResult( minQuality, (listOfRows.size()-1), null );
                
                
                expResult.setNumberOfValidPipelines(numberOfValidPipelines);
                
                

            }
            
            
            

        }
        
        
        int indexOfMinConfig = -1;
        double min10foldcv = 101.00;
        for (ConfigurationExp configurationExp : listOfConfigurationExps) {
            if (configurationExp.getListOfOneConfigurations()!=null
                    && configurationExp.getListOfOneConfigurations().size()==10
                    ) {
                
                double mean10foldcv = calculateAVGList( configurationExp.getListOfOneConfigurations()  ) ;
                
                if (mean10foldcv<min10foldcv) {
                    min10foldcv = mean10foldcv;
                    indexOfMinConfig = listOfConfigurationExps.indexOf(configurationExp);
                }
                
            }
                    
            
        }
        
        
        String str10foldcv="";
        bestPipeline = "";
        
        if (indexOfMinConfig!=-1) {
            
           
            str10foldcv = toStringCSV(listOfConfigurationExps.get(indexOfMinConfig).getListOfOneConfigurations());
            bestPipeline = listOfConfigurationExps.get(indexOfMinConfig).getListOfOneConfigurations().get(0).getBestPipeline();
        }
        
    

        return str10foldcv;

    }
    
    
    
    public ExpResult readExpForSingleFold2(String expPath, int seedNumber, boolean isBaseline) {

        File expFolder = new File(expPath);

        ExpResult expResult = null;
        
        List<ConfigurationExp> listOfConfigurationExps = new ArrayList<>();
        
        

        File modelFile = new File(expFolder.getAbsolutePath() + File.separator + "trained." + String.valueOf(seedNumber) + ".model");
        System.out.println("expFolder: " + expFolder.getAbsolutePath());
       

        {

            int numberOfValidPipelines = 0;
            
            String folderPath = expPath;
            
            
            String pipelinePath = folderPath + "\\avatar_log\\pipelines.txt";
            
            if (isBaseline && !pipelinePath.contains("adult")) {
                pipelinePath = folderPath + "\\avatar_log\\wrapper-pipelines.txt";
            }
            
            
            int seed = seedNumber;

            String runAndResultFilePath = folderPath + "\\out\\autoweka\\state-run" + seed;
            System.out.println("runAndResultFilePath: " + runAndResultFilePath);
            String[] pathnames;

            File f = new File(runAndResultFilePath);
            pathnames = f.list();

            if (!f.isDirectory()) {
                
                return null;
            } else {
                System.out.println("Dictectory");
                
            }

           

            String rsFileName = "";
            for (String pathname : pathnames) {
                // System.out.println(pathname);
                if (pathname.contains("runs_and_results") && pathname.contains(".csv")) {
                    rsFileName = pathname;

                    break;
                }
            }

            
            
            
            
            if (!rsFileName.equals("")) {
                // System.out.println("FOUND RESULT OUTPUT FILE !!!!! \n\n\n");
                rsFileName = runAndResultFilePath + "\\" + rsFileName;

                
                Double minQuality = Double.MAX_VALUE;
                int minQualityIndex = -1;
            
            
                
                CSVUtils cSVUtils = new CSVUtils();
                List<String[]> listOfRows = cSVUtils.readCSVFile(rsFileName);
                List<String[]> listOfRowsPipelines = cSVUtils.readCSVFile(pipelinePath);
                

                for (int i = 1; i < listOfRows.size(); i++) {
                    Double currentRunQuality = Double.parseDouble(listOfRows.get(i)[10]);

                    
                    
                    if (currentRunQuality<minQuality) {
                        minQuality = currentRunQuality;
                        minQualityIndex = i;
                    }
                    
                    
                    String runResults = listOfRows.get(i)[13].trim();

                    if (runResults.equals("SAT")) {
                        numberOfValidPipelines++;
                        
                        OneConfiguration oneConfiguration = new OneConfiguration();
                        oneConfiguration.setBestPipeline(listOfRowsPipelines.get(i-1)[0] );
                        
                        
                        oneConfiguration.setEstimatedError(currentRunQuality);
                        
                        String uniqueId = listOfRows.get(i)[1].trim();
                        
                        System.out.println("uniqueId: " + uniqueId);
                        
                        ConfigurationExp configurationExp = isConfigurationExist(listOfConfigurationExps, uniqueId);
                        
                        if (configurationExp==null) {
                            
                        
                            
                            List<OneConfiguration> listOfOneConfiguration = new ArrayList<>();
                            listOfOneConfiguration.add(oneConfiguration);
                            
                            configurationExp = new ConfigurationExp();
                            configurationExp.setListOfOneConfigurations(listOfOneConfiguration);
                            configurationExp.setUniqueConfiguration(uniqueId);
                            
                            listOfConfigurationExps.add(configurationExp);
                            
                        } else {
                            
                            configurationExp.getListOfOneConfigurations().add(oneConfiguration);
                            
                        }
                        
                        
                        

                    }

                }
                
                
                
                
                expResult = new ExpResult( minQuality, (listOfRows.size()-1), listOfRowsPipelines.get(minQualityIndex-1)[0] );
                //expResult = new ExpResult( minQuality, (listOfRows.size()-1), null );
                
                
                expResult.setNumberOfValidPipelines(numberOfValidPipelines);
                
                

            }
            
            
            

        }
        
        
    

        return expResult;

    }
    
    private String toStringCSV(List<OneConfiguration> listOfOneConfigurations) {
        
        
        String str = "";
        
        double sum = 0.0;
        for (int i=0;i<listOfOneConfigurations.size();i++) {
            
            
            if (i==listOfOneConfigurations.size()-1) {
                str+=String.valueOf(listOfOneConfigurations.get(i).getEstimatedError());
                
            } else {
                str+=String.valueOf(listOfOneConfigurations.get(i).getEstimatedError())+",";
                
            }
            
        }
        
     
        
        return str;
    }
    
    
    private double calculateAVGList(List<OneConfiguration> listOfOneConfigurations) {
        
        
        double rs = 0.0;
        
        double sum = 0.0;
        for (OneConfiguration oneConfig : listOfOneConfigurations) {
            sum+=oneConfig.getEstimatedError();
        }
        
        rs = sum/listOfOneConfigurations.size();
        
        return rs;
    }
    
    
    
    public ExpResult readExpForSingleFoldBaseline(String expPath, int seedNumber) {

        File expFolder = new File(expPath);

        ExpResult expResult = null;

        //File modelFile = new File(expFolder.getAbsolutePath() + File.separator + "trained." + String.valueOf(seedNumber) + ".model");
        System.out.println("expFolder: " + expFolder.getAbsolutePath());
       

        {

            int numberOfValidPipelines = 0;
            
            String folderPath = expPath;
            
            
           // String pipelinePath = folderPath + "\\avatar_log\\pipelines.txt";
            
            
            
            int seed = seedNumber;

            String runAndResultFilePath = folderPath + "\\out\\autoweka\\state-run" + seed;
            System.out.println("runAndResultFilePath: " + runAndResultFilePath);
            String[] pathnames;

            File f = new File(runAndResultFilePath);
            pathnames = f.list();

            if (!f.isDirectory()) {
                
                return expResult;
            } else {
                System.out.println("Dictectory");
                
            }

           

            String rsFileName = "";
            for (String pathname : pathnames) {
                // System.out.println(pathname);
                if (pathname.contains("runs_and_results") && pathname.contains(".csv")) {
                    rsFileName = pathname;

                    break;
                }
            }

            
            
            
            
            if (!rsFileName.equals("")) {
                // System.out.println("FOUND RESULT OUTPUT FILE !!!!! \n\n\n");
                rsFileName = runAndResultFilePath + "\\" + rsFileName;

                
                Double minQuality = Double.MAX_VALUE;
                int minQualityIndex = -1;
            
            
                
                CSVUtils cSVUtils = new CSVUtils();
                List<String[]> listOfRows = cSVUtils.readCSVFile(rsFileName);
               // List<String[]> listOfRowsPipelines = cSVUtils.readCSVFile(pipelinePath);
                

                for (int i = 1; i < listOfRows.size(); i++) {
                    Double currentRunQuality = Double.parseDouble(listOfRows.get(i)[10]);

                    if (currentRunQuality<minQuality) {
                        minQuality = currentRunQuality;
                        minQualityIndex = i;
                    }
                    
                    
                    String runResults = listOfRows.get(i)[13].trim();

                    if (runResults.equals("SAT")) {
                        numberOfValidPipelines++;

                    }

                }
                
                
                
                
                expResult = new ExpResult( minQuality, (listOfRows.size()-1), "" );
                expResult.setNumberOfValidPipelines(numberOfValidPipelines);
                
                

            }
            
            
            

        }
        
        
        
    

        return expResult;

    }

    public int getNumberOfValidEvaluations(String folderPath, String seed) {

        int numberOfValidPipelines = 0;

        String runAndResultFilePath = folderPath + "\\out\\autoweka\\state-run" + seed;
        System.out.println("runAndResultFilePath: " + runAndResultFilePath);
        String[] pathnames;

        File f = new File(runAndResultFilePath);
        pathnames = f.list();

        if (f.isDirectory()) {
            System.out.println("Dictectory");
        }

        if (f.isFile()) {
            System.out.println("File");
        }

        String rsFileName = "";
        for (String pathname : pathnames) {
            // System.out.println(pathname);
            if (pathname.contains("runs_and_results") && pathname.contains(".csv")) {
                rsFileName = pathname;

                break;
            }
        }

        if (!rsFileName.equals("")) {
            // System.out.println("FOUND RESULT OUTPUT FILE !!!!! \n\n\n");
            rsFileName = runAndResultFilePath + "\\" + rsFileName;

            CSVUtils cSVUtils = new CSVUtils();
            List<String[]> listOfRows = cSVUtils.readCSVFile(rsFileName);

            for (int i = 1; i < listOfRows.size(); i++) {
                Double currentRunQuality = Double.parseDouble(listOfRows.get(i)[10]);

                String runResults = listOfRows.get(i)[13].trim();

                if (runResults.equals("SAT")) {
                    numberOfValidPipelines++;

                }

            }

        }

        return numberOfValidPipelines;

    }

}
