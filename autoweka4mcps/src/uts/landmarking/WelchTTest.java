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
import autoweka.utils.CSVUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.distribution.TDistribution;
import uts.aai.utils.IOUtils;

public class WelchTTest {

    public static double[] meanvar(double[] a) {
        double m = 0.0, v = 0.0;
        int n = a.length;

        for (double x : a) {
            m += x;
        }
        m /= n;

        for (double x : a) {
            v += (x - m) * (x - m);
        }
        v /= (n - 1);

        return new double[]{m, v};

    }

    public static double[] welch_ttest(double[] x, double[] y) {
        double mx, my, vx, vy, t, df, p;
        double[] res;
        int nx = x.length, ny = y.length;

        res = meanvar(x);
        mx = res[0];
        vx = res[1];

        res = meanvar(y);
        my = res[0];
        vy = res[1];

        t = (mx - my) / Math.sqrt(vx / nx + vy / ny);
        df = Math.pow(vx / nx + vy / ny, 2) / (vx * vx / (nx * nx * (nx - 1)) + vy * vy / (ny * ny * (ny - 1)));
        TDistribution dist = new TDistribution(df);
        p = 2.0 * dist.cumulativeProbability(-Math.abs(t));
        return new double[]{t, df, p};
    }

    public static void main(String[] args) {
//        double x[] = {1.0, 2.0, 2.5, 3.0};
//        double y[] = {1.0, 2.0, 3.0};
//        double res[] = welch_ttest(x, y);
//        System.out.println("t = " + res[0]);
//        System.out.println("df = " + res[1]);
//        System.out.println("p = " + res[2]);
//        

        String reduction_method_mean_error = "C:\\experiments\\results\\welch-test-dataset-method-50-folds\\methods\\reduction_method_mean_error.csv";

        CSVUtils csv2 = new CSVUtils();
        List<String[]> content_mean_error = csv2.readCSVFile(reduction_method_mean_error);

        List<Double[][]> allDatasetResults = new ArrayList<>();

        List<String> listOfMethods = new ArrayList<>();

        listOfMethods.add("r30");
        listOfMethods.add("avatar");
        listOfMethods.add("baseline");

        listOfMethods.add("M1-k1");
        listOfMethods.add("M1-k4");
        listOfMethods.add("M1-k8");
        listOfMethods.add("M1-k10");
        listOfMethods.add("M1-k19");

        listOfMethods.add("M2-k1");
        listOfMethods.add("M2-k4");
        listOfMethods.add("M2-k8");
        listOfMethods.add("M2-k10");
        listOfMethods.add("M2-k19");

        listOfMethods.add("L1-k1");
        listOfMethods.add("L1-k4");
        listOfMethods.add("L1-k8");
        listOfMethods.add("L1-k10");
        listOfMethods.add("L1-k19");

        listOfMethods.add("L2-k1");
        listOfMethods.add("L2-k4");
        listOfMethods.add("L2-k8");
        listOfMethods.add("L2-k10");
        listOfMethods.add("L2-k19");

        listOfMethods.add("O1-k1");
        listOfMethods.add("O1-k4");
        listOfMethods.add("O1-k8");
        listOfMethods.add("O1-k10");
        listOfMethods.add("O1-k19");

        listOfMethods.add("O2-k1");
        listOfMethods.add("O2-k4");
        listOfMethods.add("O2-k8");
        listOfMethods.add("O2-k10");
        listOfMethods.add("O2-k19");

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

        for (int d = 0; d < listOfDatasets.size(); d++) {

            Double[][] datasetMethodWelchTestResult = new Double[listOfMethods.size()][listOfMethods.size()];

            for (int i = 0; i < listOfMethods.size(); i++) {
                for (int j = 0; j < listOfMethods.size(); j++) {

                    //if (i != j) 
                    {

                        String exp_root_folder = "C:\\experiments\\results\\welch-test-dataset-method-50-folds\\methods\\";
                        String fileName1 = exp_root_folder + listOfMethods.get(i) + ".csv";
                        String fileName2 = exp_root_folder + listOfMethods.get(j) + ".csv";

                        CSVUtils csv1 = new CSVUtils();
                        List<String[]> content1 = csv1.readCSVFile(fileName1);
                        List<String[]> content2 = csv1.readCSVFile(fileName2);

                        List<Double> xList = new ArrayList<>();

                        for (int z = 1; z < content1.get(d).length; z++) {
                            if (!content1.get(d)[z].equals("")) {
                                xList.add(Double.parseDouble(content1.get(d)[z]));
                            }
                        }

                        // sort before test
                        Collections.sort(xList);

                        double[] x = new double[xList.size()];

                        for (int z = 1; z < xList.size(); z++) {
                            x[z] = xList.get(z);
                        }

                        List<Double> yList = new ArrayList<>();

                        for (int z = 1; z < content2.get(d).length; z++) {
                            if (!content2.get(d)[z].equals("")) {
                                yList.add(Double.parseDouble(content2.get(d)[z]));
                            }
                        }

                        // sort before test
                        Collections.sort(yList);

                        double[] y = new double[yList.size()];

                        for (int z = 1; z < yList.size(); z++) {
                            y[z] = yList.get(z);
                        }

                        double res[] = welch_ttest(x, y);
                        datasetMethodWelchTestResult[i][j] = res[2];

                    }

                }

            }

            allDatasetResults.add(datasetMethodWelchTestResult);

        }

        for (int d = 0; d < listOfDatasets.size(); d++) {

            String outputStringDataSet = "";

            //System.out.print(listOfDatasets.get(d));
            outputStringDataSet += listOfDatasets.get(d);

            for (int i = 0; i < listOfMethods.size(); i++) {
                //System.out.print(","+listOfMethods.get(i));
                outputStringDataSet += "," + listOfMethods.get(i);
            }

            //System.out.print("\t"+"mean_error");
            //System.out.println("");
            outputStringDataSet += "," + "mean_error\n";

            for (int i = 0; i < listOfMethods.size(); i++) {
                //System.out.print(listOfMethods.get(i)+"\t");
                outputStringDataSet += listOfMethods.get(i) + ",";

                for (int j = 0; j < listOfMethods.size(); j++) {
                    //System.out.print(allDatasetResults.get(d)[i][j]+"\t");
                    outputStringDataSet += allDatasetResults.get(d)[i][j] + ",";
                }

                //System.out.print(content_mean_error.get(d+1)[i+1]);
                //System.out.println("");
                outputStringDataSet += content_mean_error.get(d + 1)[i + 1] + "\n";

            }

            {
                //System.out.print("mean_error");
                outputStringDataSet += "mean_error";

                for (int j = 0; j < listOfMethods.size(); j++) {
                    //  System.out.print("\t" + content_mean_error.get(d+1)[j+1]);
                    outputStringDataSet += "," + content_mean_error.get(d + 1)[j + 1];
                }

                // System.out.println("");
            }

            IOUtils iOUtils = new IOUtils();
            iOUtils.overWriteData(outputStringDataSet, "C:\\experiments\\results\\tmp.csv");

            CSVUtils csvu3 = new CSVUtils();
            List<String[]> lines = csvu3.readCSVFile("C:\\experiments\\results\\tmp.csv");

            String[][] array = new String[lines.size()][0];
            lines.toArray(array);

            array = sort2DArray(array);
            String lastLine = groupTopPredictor(array);

            System.out.println("");
            //System.out.println(Arrays.deepToString(array));
            //System.out.println(outputStringDataSet);

            String outputStringDataSet2 = "";
            for (String[] strings : array) {
                for (String string : strings) {
                    //System.out.print("\t" + string);
                    outputStringDataSet2 += string + ",";
                }
                //System.out.println();
                outputStringDataSet2 += "\n";
            }

            outputStringDataSet2 += lastLine;

            String outputFileName = "C:\\experiments\\results\\welch-test-dataset-method-50-folds\\method_performance\\" + listOfDatasets.get(d) + ".csv";
            IOUtils iOUtils1 = new IOUtils();
            iOUtils1.overWriteData(outputStringDataSet2, outputFileName);

            System.out.println(outputStringDataSet2);
            //   System.out.print(lastLine);
            System.out.println("");
        }

    }

    public static String groupTopPredictor(String[][] array) {

        String lastLine = "";
        boolean isStop = false;

        for (int k = 1; k <= 33; k++) {

            Double d = Double.parseDouble(array[1][k]);
            if (!isStop) {
                if (d >= 0.95) {
                    lastLine += "," + "1";

                } else {

                    lastLine += "," + "0";
                    isStop = true;
                }

            } else {
                lastLine += "," + "0";
            }

        }

        return lastLine;
    }

    public static String[][] sort2DArray(String[][] array) {
        // sort by p values

        {
            // i row
            // j col
            for (int i = 0; i < 32; i++) {
                for (int j = i + 1; j < 33; j++) {

                    Double d_i = Double.parseDouble(array[i + 1][1]);
                    Double d_j = Double.parseDouble(array[j + 1][1]);

                    if (d_i > d_j) {
                        // swap
                        for (int k = 0; k < 35; k++) {

                            String tempCell = array[i + 1][k];
                            array[i + 1][k] = array[j + 1][k];
                            array[j + 1][k] = tempCell;
                        }
                    }
                }
            }
            
            
            
            for (int i = 0; i < 32; i++) {
                for (int j = i + 1; j < 33; j++) {

                    Double d_i = Double.parseDouble(array[1][i + 1]);
                    Double d_j = Double.parseDouble(array[1][j + 1]);

                    if (d_i > d_j) {
                        // swap
                        for (int k = 0; k < 35; k++) {

                            String tempCell = array[k][i + 1];
                            array[k][i + 1] = array[k][j + 1];
                            array[k][j + 1] = tempCell;
                        }
                    }
                }
            }
            
            
        }

        // sort by mean error
        {

            // i row
            // j col
            for (int i = 0; i < 32; i++) {
                for (int j = i + 1; j < 33; j++) {

                    Double d_i = Double.parseDouble(array[i + 1][34]);
                    Double d_j = Double.parseDouble(array[j + 1][34]);

                    if (d_i > d_j) {
                        // swap
                        for (int k = 0; k < 35; k++) {

                            String tempCell = array[i + 1][k];
                            array[i + 1][k] = array[j + 1][k];
                            array[j + 1][k] = tempCell;
                        }
                    }
                }
            }

            for (int i = 0; i < 32; i++) {
                for (int j = i + 1; j < 33; j++) {

                    Double d_i = Double.parseDouble(array[34][i + 1]);
                    Double d_j = Double.parseDouble(array[34][j + 1]);

                    if (d_i > d_j) {
                        // swap
                        for (int k = 0; k < 35; k++) {

                            String tempCell = array[k][i + 1];
                            array[k][i + 1] = array[k][j + 1];
                            array[k][j + 1] = tempCell;
                        }
                    }
                }
            }

        }

        return array;
    }
}
