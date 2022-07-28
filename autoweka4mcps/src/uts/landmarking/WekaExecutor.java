/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.landmarking;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.avatar.optimisation.RandomSearch;

import uts.aai.avatar.configuration.AppConst;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponentType;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import org.apache.commons.io.FileUtils;
import uts.aai.avatar.model.MLComponent;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author ntdun
 */
public class WekaExecutor {

    public WekaExecutor() {
    }

    public boolean executeAlgorithm(String inputData, String outputData, MLComponent mLComponent) {

        boolean result = true;

        if (mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER)
                || mLComponent.getmLComponentType().equals(MLComponentType.REGRESSOR)
                || mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER_REGRESSOR)
                || mLComponent.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {

            result = executePredictor(inputData, outputData, mLComponent);

        } else {

            result = executeFilter(inputData, outputData, mLComponent);

        }

        File file = new File(outputData);
        System.out.println("Output Length: " + file.length());
        if (file.length() == 0) {
            result = false;
        }
        return result;

    }

    public boolean executeFilter(String inputData, String outputData, MLComponent mLComponent) {
        System.out.println("executeFilter xxxxxxxxxxxx");
        boolean result = true;

        if (Files.notExists(new File(inputData).toPath())) {
            result = false;
            System.out.println("No Input File ------------------------:" + inputData);
        }

        if (result) {

            try {

                String wekajar = AppConst.WEKA_JAR_PATH;

                String commandStr = "java -classpath " + wekajar + " "
                        + mLComponent.getComponentExecutionScriptSingleComponentWeka()
                        + " -i " + inputData
                        + " -o " + outputData;

                //commandStr = "java -classpath C:/experiments/tools/avatar/weka.jar weka.filters.unsupervised.attribute.IndependentComponents -W -A -1 -N 200 -T 1.0E-4 -i C:/experiments/tools/avatar/temp-3.arff -o C:/experiments/tools/avatar/output/temp-data-out-4.arff";
                System.out.println("command: \n" + commandStr);
                Process process = Runtime.getRuntime().exec(commandStr);
                System.out.println("Waiting for batch file ...");

                isTimeout(process, mLComponent, outputData);

                System.out.println("Batch file done.");
                System.out.println("Done! " + mLComponent.getComponentId());
                process.destroyForcibly();

            } catch (Exception ex) {
                Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            if (Files.notExists(new File(outputData).toPath())
                    || (new File(outputData).length() < 1)) {
                result = false;
            }

        }
        return result;
    }

    public boolean executePredictor(String inputData, String outputModel, MLComponent mLComponent) {
        IOUtils iou = new IOUtils();
        iou.overWriteData("false", AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
        System.out.println("executePredictor xxxxxxxxxxxx");
        boolean result = true;
        boolean isTimeout = false;
        if (Files.notExists(new File(inputData).toPath())) {
            result = false;
            System.out.println("No Input File ------------------------:" + inputData);
        }

        if (result) {

            try {

                String wekajar = AppConst.WEKA_JAR_PATH;

                String commandConfig = mLComponent
                        .getComponentExecutionScriptSingleComponentWeka();

                String argumentStr1 = "";
                String argumentStr2 = "";

                if (commandConfig.contains(" ")) {
                    String[] cmds = commandConfig.split(" ", 2);
                    argumentStr1 = cmds[0];
                    argumentStr2 = cmds[1];
                } else {
                    argumentStr1 = commandConfig;
                }

                String commandStr = "java -classpath " + wekajar + " "
                        + argumentStr1
                        + " -t " + inputData
                        + " -d " + outputModel + " "
                        + argumentStr2 + "\n";
                System.out.println(commandStr);
                Process process = Runtime.getRuntime().exec(commandStr);

                System.out.println("Waiting for batch file ...");

                isTimeout = isTimeout(process, mLComponent, outputModel);

                System.out.println("Batch file done.");
                System.out.println("Done! " + mLComponent.getComponentId());

            } catch (Exception ex) {
                System.out.println(ex);
                return false;
            }

//            Double accuracy = evaluateModel(outputModel, inputData);
//            
//           
//            if (accuracy != null) {
//                System.out.println("ACCURACY: " + accuracy);
//                iou.overWriteData(String.valueOf(accuracy),AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
//                result = true;
//            } else {
//                result = false;
//            }
            if (Files.notExists(new File(outputModel).toPath())
                    || (new File(outputModel).length() < 1)) {
                result = false;
            }

            if (isTimeout) {
                iou.overWriteData("timeout", AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
            } else {
                iou.overWriteData(String.valueOf(result), AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
            }

            // cleanTemp();
        }
        return result;
    }

    public double executePredictor(String inputData, Classifier cls) {
        double errorRate = -1.0;
        

        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputData));
            Instances train = new Instances(reader);
            train.setClassIndex(train.numAttributes() - 1);
            // train classifier
            //   Classifier cls = new J48();

            cls.buildClassifier(train);
            // evaluate classifier and print some statistics

            Evaluation eval = new Evaluation(train);
            eval.crossValidateModel(cls, train, 10, new Random(1));
            //  eval.evaluateModel(cls, train);
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));

            errorRate = eval.errorRate();

            System.out.println("Classification Error: " + eval.errorRate());
        } catch (Exception ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return errorRate;
    }

    public List<Double> executePredictor2(String inputData, Classifier cls) {
        double errorRate = -1.0;
        List<Double> listOfErrorRate  = new ArrayList<>();
        
        try {

            Instances data1 = DataSource.read(inputData); // upload the dataset
            data1.setClassIndex(data1.numAttributes() - 1); // set class index
            data1.randomize(new Random(1));
            data1.stratify(10); // stratify the dataset into 10 folds
            for (int i = 0; i < 10; i++) {
                Instances train = data1.trainCV(10, i);
                Instances test = data1.testCV(10, i);
                System.out.println("istances: " + train.numInstances() + " - "+ test.numInstances());
                cls.buildClassifier(train);
                Evaluation eval = new Evaluation(train);
                eval.evaluateModel(cls, test);
                System.out.println("Fold: " + i);
                System.out.println(eval.toSummaryString("\nResults\n======\n", false));

                errorRate = eval.errorRate();
                listOfErrorRate.add(errorRate);
                System.out.println("Classification Error: " + eval.errorRate());
                
                
                String model_path = "C:\\experiments\\models\\m" +String.valueOf(i) + ".model";
                
                weka.core.SerializationHelper.write(model_path, cls);

              
            }

           
        } catch (Exception ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return listOfErrorRate;
    }

    public boolean executePredictor(String inputData, String outputModel, String commandConfig) {
        IOUtils iou = new IOUtils();
        iou.overWriteData("false", AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
        System.out.println("executePredictor xxxxxxxxxxxx");
        boolean result = true;
        boolean isTimeout = false;
        if (Files.notExists(new File(inputData).toPath())) {
            result = false;
            System.out.println("No Input File ------------------------:" + inputData);
        }

        if (result) {

            try {

                String wekajar = AppConst.WEKA_JAR_PATH;

                //String commandConfig = mLComponent
                //      .getComponentExecutionScriptSingleComponentWeka();
                String argumentStr1 = "";
                String argumentStr2 = "";

                if (commandConfig.contains(" ")) {
                    String[] cmds = commandConfig.split(" ", 2);
                    argumentStr1 = cmds[0];
                    argumentStr2 = cmds[1];
                } else {
                    argumentStr1 = commandConfig;
                }

                String commandStr = "java -classpath " + wekajar + " "
                        + argumentStr1
                        + " -t " + inputData
                        + " -d " + outputModel + " "
                        + argumentStr2 + "\n";
                System.out.println(commandStr);
                Process process = Runtime.getRuntime().exec(commandStr);

                String outputStr = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = in.readLine()) != null) {
                    // outputStr+=outputStr+"\n";

                    System.out.println(line);
                }

                System.out.println("Waiting for batch file ...");

                isTimeout = isTimeout2(process, outputModel);

                System.out.println("Batch file done.");
                System.out.println("Done! " + commandConfig);

                System.out.println("Output: " + outputStr);

            } catch (Exception ex) {
                System.out.println(ex);
                return false;
            }

//            Double accuracy = evaluateModel(outputModel, inputData);
//            
//           
//            if (accuracy != null) {
//                System.out.println("ACCURACY: " + accuracy);
//                iou.overWriteData(String.valueOf(accuracy),AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
//                result = true;
//            } else {
//                result = false;
//            }
            if (Files.notExists(new File(outputModel).toPath())
                    || (new File(outputModel).length() < 1)) {
                result = false;
            }

            if (isTimeout) {
                iou.overWriteData("timeout", AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
            } else {
                iou.overWriteData(String.valueOf(result), AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
            }

            // cleanTemp();
        }
        return result;
    }

    private void cleanTemp() {

        File directory = new File(AppConst.TEMP_OUTPUT_PATH);
        try {
            FileUtils.cleanDirectory(directory);

        } catch (IOException ex) {

        }
    }

    // ///////////////////////////////////////////
    private class InterruptScheduler extends TimerTask {

        Thread target = null;

        public InterruptScheduler(Thread target) {
            this.target = target;
        }

        @Override
        public void run() {
            target.interrupt();
        }

    }

    public Double evaluateModel(String modelPath, String validationSet) {
        try {
            if (!Files.notExists(new File(modelPath).toPath())) {

                System.out.println("modelPath:-" + modelPath + "-");
                System.out.println("validationSet:-" + validationSet + "-");

                Double accuracy = null;
                try (InputStream inputstreammodelPath = new FileInputStream(modelPath)) {
                    Classifier cls = (Classifier) weka.core.SerializationHelper.read(inputstreammodelPath);
                    try (InputStream inputstreamValidationSet = new FileInputStream(validationSet)) {
                        DataSource source = new DataSource(inputstreamValidationSet);
                        Instances validationData = source.getDataSet();
                        validationData.setClassIndex(validationData.numAttributes() - 1);
                        Evaluation evaluation = new Evaluation(validationData);
                        evaluation.evaluateModel(cls, validationData);
                        accuracy = evaluation.pctCorrect();
                        inputstreamValidationSet.close();
                    }

                    inputstreammodelPath.close();
                }

                return accuracy;
            }
        } catch (Exception ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean isTimeout(Process process, MLComponent mLComponent, String outputPath) {

        boolean isTimeout = false;
        long startTime = System.currentTimeMillis();

        while (process.isAlive()) {

            System.out.println("... waiting ...");

            if (mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER)
                    || mLComponent.getmLComponentType().equals(MLComponentType.REGRESSOR)
                    || mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER_REGRESSOR)
                    || mLComponent.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {
                if (!Files.notExists(new File(outputPath).toPath())) {
                    process.destroyForcibly();
                    break;
                }
            }

            long currentTime = System.currentTimeMillis();

            if ((currentTime - startTime) > (AppConst.EXECUTION_TIMEOUT * 60 * 1000)) {
                System.out.println("TIME OUT");
                process.destroyForcibly();
                isTimeout = true;
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        process.destroyForcibly();
        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isTimeout;
    }

    private boolean isTimeout2(Process process, String outputPath) {

        boolean isTimeout = false;
        long startTime = System.currentTimeMillis();

        while (process.isAlive()) {

            System.out.println("... waiting ...");

            if (!Files.notExists(new File(outputPath).toPath())) {
                process.destroyForcibly();
                break;
            }

            long currentTime = System.currentTimeMillis();

            if ((currentTime - startTime) > (AppConst.EXECUTION_TIMEOUT * 60 * 1000)) {
                System.out.println("TIME OUT");
                process.destroyForcibly();
                isTimeout = true;
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        process.destroyForcibly();
        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isTimeout;
    }

}
