/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.landmarking;

import autoweka.utils.IOUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.inference.TTest;
import static uts.landmarking.GenerateAnalysisReportOfMeanRanking.extractShortClasifierName;
import static uts.landmarking.GenerateAnalysisReportOfMeanRanking.isPredictorExist;

/**
 *
 * @author ntdun
 */
public class GenerateAnalysisReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //String outputFileBestPPPath = "C:\\experiments\\results\\welch-test-dataset-method-50-folds\\pipelines\\avatar.csv";
        boolean isBaseline = false;
        
        
        List<String> listOfDatasets = new ArrayList<>();
        listOfDatasets.add("abalone");
        listOfDatasets.add("adult");
        listOfDatasets.add("amazon");
        listOfDatasets.add("car");
        listOfDatasets.add("cifar10small");
        listOfDatasets.add("convex");
        listOfDatasets.add("dexter");
        listOfDatasets.add("dorothea");
        listOfDatasets.add("gcredit");
        listOfDatasets.add("gisette");
        listOfDatasets.add("kddcup");
        listOfDatasets.add("krvskp");
        listOfDatasets.add("madelon");
        listOfDatasets.add("mnist");
        listOfDatasets.add("secom");
        listOfDatasets.add("semeion");
        listOfDatasets.add("shuttle");
        listOfDatasets.add("waveform");
        listOfDatasets.add("winequality");
        listOfDatasets.add("yeast");

        ExpResult[][] listOfExpResults = new ExpResult[listOfDatasets.size()][5];

        List<List<Double>> listOfDatasetPtest = new ArrayList<>();
        List<List<Double>> listOfDatasetAllRuns = new ArrayList<>();

        for (String dataSetName : listOfDatasets) {

            List<double[]> listOfRuns = new ArrayList<>();
            List<Double> listOfRunsAllSeeds = new ArrayList<>();

            for (int j = 0; j < 5; j++) {

                // add x
                int x_index = 8;
                String expName = "add"+String.valueOf(x_index)+"_" + dataSetName + "_seed" + String.valueOf(j);
                String expFilePath = "E:\\addx\\" + expName;
                
                Autoweka4MCPSExperimentAnalysis amcpsea = new Autoweka4MCPSExperimentAnalysis();
                ExpResult expResult = amcpsea.readExpFor10foldcv(expFilePath, j);
                listOfExpResults[listOfDatasets.indexOf(dataSetName)][j] = expResult;

                /////////////////// ttest
                Autoweka4MCPSExperimentAnalysis_V2 amcpsea_v2 = new Autoweka4MCPSExperimentAnalysis_V2();
                String strsCSV = amcpsea_v2.readExpForSingleFold(expFilePath, j, isBaseline);

                String bestPP = amcpsea_v2.getBestPipeline();

                System.out.println("strsCSV: " + strsCSV);

                {
                    if (!strsCSV.equals("")) {
                        String[] strs = strsCSV.split(",");
                        double[] strd = new double[strsCSV.length()];

                        for (int i = 0; i < strs.length; i++) {
                            strd[i] = Double.parseDouble(strs[i]);
                        }

                        for (int z = 0; z < strs.length; z++) {
                            strd[z] = Double.parseDouble(strs[z]);
                            listOfRunsAllSeeds.add(Double.parseDouble(strs[z]));
                        }

                        listOfRuns.add(strd);

                    }

                }

                /////////////////// end ttest
            }

            //////////////  ttest
            System.out.println("listOfRuns.size(): " + listOfRuns.size());
            List<Double> listOfPtests = new ArrayList<>();

            for (int j = 0; j < listOfRuns.size(); j++) {
                for (int m = j + 1; m < listOfRuns.size(); m++) {

                    if (j != m) {
                        TTest tTest = new TTest();
                        double p_i = tTest.tTest(listOfRuns.get(j), listOfRuns.get(m));
                        listOfPtests.add(p_i);
                        System.out.println("p_i:" + p_i);
                    }

                }
            }

            listOfDatasetPtest.add(listOfPtests);
            listOfDatasetAllRuns.add(listOfRunsAllSeeds);
            ////////////// end  ttest

        }

        //for (int k=0;k<5;k++) 
        {

            for (int i = 0; i < listOfDatasets.size(); i++) {

                for (int j = 0; j < 5; j++) {

                    if (listOfExpResults[i][j] != null) {

                        System.out.print(listOfExpResults[i][j].getEstimatedError() + "\t\t"
                                + listOfExpResults[i][j].getNumberOfValidPipelines() + "\t\t");

                    } else {
                        System.out.print("-" + "\t\t"
                                + "-" + "\t\t");

                    }

                }
                System.out.println("");

            }

        }

        /////////////////////////////////// ALL BEST PIPELINES OF ALL RUNS
        System.out.println("[ALL BEST PIPELINES OF ALL RUNS]");
        System.out.println("\n\n\n");
        {

            String outputAllBestPipelines = "";

            //System.out.println("k: " + k);
            for (int i = 0; i < listOfDatasets.size(); i++) {

                //  System.out.print(listOfDatasets.get(i));
                List<PredictorPair> listOfPredictorsForOneDataset = new ArrayList<>();

                for (int j = 0; j < 5; j++) {

                    if (listOfExpResults[i][j] != null) {
                        //System.out.print("\t" + listOfExpResults[k][i][j].getBestPipeline());
                        List<String> listOfPredictorsForOneSeed = extractShortClasifierName(listOfExpResults[i][j].getBestPipeline());

                        for (String predictorSeed : listOfPredictorsForOneSeed) {

                            PredictorPair foundPredictorPair = isPredictorExist(listOfPredictorsForOneDataset, predictorSeed);

                            if (foundPredictorPair == null) {
                                foundPredictorPair = new PredictorPair(predictorSeed, 1);
                                listOfPredictorsForOneDataset.add(foundPredictorPair);

                            } else {
                                foundPredictorPair.setCounter(foundPredictorPair.getCounter() + 1);
                            }

                        }

                        //System.out.print("\t" + extractShortClasifierName(listOfExpResults[k][i][j].getBestPipeline()));
                    } else {
                        //System.out.print("\t" + "-");
                    }

                }

                System.out.print(listOfDatasets.get(i) + "\t");
                outputAllBestPipelines += listOfDatasets.get(i) + ",";

                for (int j = 0; j < listOfPredictorsForOneDataset.size(); j++) {

                    PredictorPair predictorPair = listOfPredictorsForOneDataset.get(j);

                    if (j == listOfPredictorsForOneDataset.size() - 1) {

                        System.out.print(String.valueOf(predictorPair.getCounter()) + predictorPair.getPredictorName());
                        outputAllBestPipelines += String.valueOf(predictorPair.getCounter()) + predictorPair.getPredictorName() + ",";
                    } else {

                        System.out.print(String.valueOf(predictorPair.getCounter()) + predictorPair.getPredictorName() + ";");
                        outputAllBestPipelines += String.valueOf(predictorPair.getCounter()) + predictorPair.getPredictorName() + ";";

                    }

                }
                outputAllBestPipelines += "\n";
                System.out.println("");
            }

            System.out.println("\n\n\n");

            IOUtils iOUtils2 = new IOUtils();
            //iOUtils2.overWriteData(outputAllBestPipelines, outputFileBestPPPath);
        }

        System.out.println("[Best Pipeline]");

        {

            for (int i = 0; i < listOfDatasets.size(); i++) {

                int lowest_j = 0;
                for (int j = 0; j < 5; j++) {

                    if (listOfExpResults[i][lowest_j] == null && listOfExpResults[i][j] != null) {

                        lowest_j = j;

                    }

                    if (listOfExpResults[i][j] != null && listOfExpResults[i][lowest_j] != null) {

                        if (listOfExpResults[i][j].getEstimatedError() < listOfExpResults[i][lowest_j].getEstimatedError()) {
                            lowest_j = j;
                        }

                    }

                }

                if (listOfExpResults[i][lowest_j] != null) {
                    System.out.println(listOfExpResults[i][lowest_j].getBestPipeline());
                } else {
                    System.out.println("-");
                }
                //System.out.println("");

            }

        }

        //////////// start ttest
        System.out.println("PTest: ");

        for (int i = 0; i < listOfDatasets.size(); i++) {

            System.out.print(listOfDatasets.get(i) + "\t");

            for (int j = 0; j < listOfDatasetPtest.get(i).size(); j++) {

                System.out.print(listOfDatasetPtest.get(i).get(j) + "\t");

            }

            System.out.println("");

        }

        /////// end ttest
        //////////// start ttest
        System.out.println("\n\n\n\n\nAll  Runs: \n");

        for (int i = 0; i < listOfDatasets.size(); i++) {

            System.out.print(listOfDatasets.get(i));

            for (int j = 0; j < listOfDatasetAllRuns.get(i).size(); j++) {
                System.out.print("\t" + listOfDatasetAllRuns.get(i).get(j));
            }

            System.out.println("");

        }

    }

}
