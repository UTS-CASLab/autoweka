package autoweka;

import avatar.config.LoggerUtil;
import javax.xml.bind.annotation.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "experiment")
@XmlAccessorType(XmlAccessType.NONE)
public class Experiment extends XmlSerializable {

    /**
     * The name of the experiment, designed to be human readable and contain
     * useful ID information
     */
    @XmlElement(name = "name")
    public String name;
    /**
     * A helper variable that indicates the type of the experiment - this is set
     * by the ExperimentConstructor
     */
    @XmlElement(name = "type")
    public String type;
    /**
     * Use the AVATAR to pre-evaluate pipelines
     */
    @XmlElement(name = "isAvatar")
    public boolean isAvatar = false;
    /**
     * The name of the result metric to use - see ClassiferResult.Metric
     */
    @XmlElement(name = "numberOfInitConfigs")
    public int numberOfInitConfigs = 1;
    /**
     * The name of the result metric to use - see ClassiferResult.Metric
     */
    
    @XmlElement(name = "resultMetric")
    public String resultMetric = "errorRate";
    /**
     * The class name of the instance generator
     */
    @XmlElement(name = "instanceGenerator")
    public String instanceGenerator;
    /**
     * The property string that contains the construction arguments for the
     * instance generator
     */
    @XmlElement(name = "instanceGeneratorArgs")
    public String instanceGeneratorArgs;
    //@XmlElement(name="regularizer")
    //public String regularizer;
    //@XmlElement(name="regularizerArgs")
    //public String regularizerArgs;
    /**
     * The string that indicates what dataset is going to be used
     */
    @XmlElement(name = "datasetString")
    public String datasetString;
    /**
     * Boolean indicating if Attribute/Feature selection should happen
     */
    @XmlElement(name = "attributeSelection")
    public boolean attributeSelection = false;
    /**
     * How many seconds should be spent performing attribute selection?
     */
    @XmlElement(name = "attributeSelectionTimeout")
    public float attributeSelectionTimeout = -1;
    /**
     * The name of the class that's going to be used to parse the trajectory -
     * this is set by the ExperimentConstructor
     */
    @XmlElement(name = "trajectoryParserClass")
    public String trajectoryParserClassName;
    /**
     * The list of command line arguments that are used to invoke the SMBO
     * method - this is set by the ExperimentConstructor
     */
    @XmlElement(name = "callString")
    public List<String> callString = new ArrayList<String>();
    /**
     * A list of var=values that are set in environment variables before the
     * SMBO method is executed - these are set by the ExperimentConstructor
     */
    @XmlElement(name = "envVariables")
    public List<String> envVariables = new ArrayList<String>();
    /**
     * How many seconds should be spent for this experiment overall? (IE the
     * SMBO method's budget)
     */
    @XmlElement(name = "tunerTimeout")
    public float tunerTimeout = -1;
    /**
     * How many seconds should be spent training a set of hyperparameters on a
     * specific partition of the training and test data?.
     *
     * Note that this is more of a guideline, Auto-WEKA has quite a bit of slack
     * built into to accomodate classifiers that can return partial results, or
     * ones that decide to offload training onto their evaluation phase.
     */
    @XmlElement(name = "trainTimeout")
    public float trainTimeout = -1;
    /**
     * The string passed to the Xmx argument of a sub process limiting the RAM
     * that WEKA will have
     */
    @XmlElement(name = "memory")
    public String memory;
    /**
     * Any extra properties that are associated with the experiment, these are
     * generally for the constructor or your wrappers
     */
    @XmlElement(name = "extraProps")
    public String extraPropsString;
    /**
     * Forces Auto-WEKA to only use the list of classifiers here - if it is
     * empty, then Auto-WEKA will try to use everything that it can
     */
    @XmlElement(name = "allowedClassifiers")
    public List<String> allowedClassifiers = new ArrayList<String>();
    /**
     * Forces Auto-WEKA to only use the list of fitlers here - if it is empty,
     * then Auto-WEKA will try to use everything that it can
     */
    @XmlElement(name = "allowedFilters")
    public List<String> allowedFilters = new ArrayList<String>();
    /**
     * For analysis experiements, these extra classifier evaluations will be
     * done for every point along the trajectory.
     *
     * For example, if we want to look at the Testing performance all the way
     * along, we would add a new TrajectoryPointExtra that contains an instance
     * string of 'default', since that causes Auto-WEKA to preserve the provided
     * training/test split
     */
    @XmlElement(name = "trajectoryPointExtras")
    public List<TrajectoryPointExtra> trajectoryPointExtras = new ArrayList<TrajectoryPointExtra>();

    /**
     * Class capturing any of the extra dataset partitions that should be run on
     * all the trajectory points for analysis
     */
    @XmlRootElement(name = "trajpointextra")
    @XmlAccessorType(XmlAccessType.NONE)
    static public class TrajectoryPointExtra {

        public TrajectoryPointExtra() {
        }

        public TrajectoryPointExtra(String _name, String _instance) {
            name = _name;
            instance = _instance;
        }

        /**
         * The human readable name corresponding to this instance ie. 'testing'
         * or 'fold0'
         */
        @XmlElement(name = "name")
        public String name;
        /**
         * The instance string that is passed to the instance generator to
         * evaluate on
         */
        @XmlElement(name = "instance")
        public String instance;
    }

    public Experiment() {
    }

    /**
     * Throws a runtime exception if the experiment contains some sort of crazy
     * values.
     */
    public void validate() {
        if (this.name == null) {
            throw new RuntimeException("No experiment -name was defined!");
        }
        if (this.trainTimeout < 0) {
            throw new RuntimeException("Need a -traintimeout > 0!");
        }
        if (this.tunerTimeout < 0) {
            throw new RuntimeException("Need a -tunertimeout > 0!");
        }
        if (this.datasetString == null) {
            throw new RuntimeException("Need an -instancezip file!");
        }
        if (this.instanceGenerator == null) {
            throw new RuntimeException("Need an -instancegenerator");
        }
        if (this.attributeSelection && this.attributeSelectionTimeout < 0) {
            throw new RuntimeException("Need a -attributeselectiontimeout > 0 since attribute selection is on!");
        }
    }

    /**
     * Static method to load up an experiment from XML.
     */
    public static Experiment fromXML(String filename) {
        return XmlSerializable.fromXML(filename, Experiment.class);
    }

    /**
     * Static method to load up an experiment from XML.
     */
    public static Experiment fromXML(InputStream xml) {
        return XmlSerializable.fromXML(xml, Experiment.class);
    }

    /**
     * Main method to actually run an experiment with a given seed.
     *
     * This requires two command line arguments, the path to the experiment
     * folder (but not the actuall .experiment itself) and the seed on which you
     * want to run this experiment. The seed is passed to the SMBO method to
     * allow for easy paralellization of each SMBO method.
     *
     * Takes an optional argument '-silent' that doesn't print everything out to
     * stdout
     *
     */
    public static void main(String[] args) {
        //Load an experiment and seed from the args
        File experiment = null;
        String seed = null;
        boolean silent = false;
        boolean noExit = false;
        File expFolder = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-silent")) {
                silent = true;
            } else if (args[i].equals("-noexit")) {
                noExit = true;
            } else if (experiment == null) {
                //Get the experiment folder
                expFolder = new File(args[i]).getAbsoluteFile();
                //Get the actuall experiment
                experiment = new File(expFolder.getAbsolutePath() + File.separator + expFolder.getName() + ".experiment");
            } else if (seed == null) {
                seed = args[i];
            } else {
                throw new RuntimeException("Unknown arg: " + args[i]);
            }
        }
        if (experiment == null || seed == null) {
            throw new RuntimeException("Need to be run with an experiment and a seed");
        }

        try {
            Experiment exp = Experiment.fromXML(new FileInputStream(experiment));

            for (int i = 0; i < exp.callString.size(); i++) {
                exp.callString.set(i, exp.callString.get(i).replace("{SEED}", seed));
                System.out.print("'" + exp.callString.get(i) + "' ");
            }

            System.out.println();

            //See if we can get the path
            File executable = autoweka.Util.findExecutableOnPath(exp.callString.get(0));
            if (executable == null) {
                throw new RuntimeException("Failed to find the executable '" + exp.callString.get(0) + "'");
            }

            exp.callString.set(0, executable.getAbsolutePath());

           // String smacCommand = prepareSMACCommand(exp);
           // smacCommand = smacCommand.replace("\\", "/");
           // LoggerUtil.logAvatar("smac_command: " + smacCommand);
            
            //File dir = new File("C:/Users/username/Desktop/Sample");
            //String cmd = "java -jar BatchSample.jar";
            //Process process = Runtime.getRuntime().exec(cmd, null, dir);
            
           ProcessBuilder pb = new ProcessBuilder(exp.callString);
          // ProcessBuilder pb = new ProcessBuilder(smacCommand);
           pb.directory(experiment.getParentFile());
            pb.redirectErrorStream(true);

            java.util.Map<String, String> env = pb.environment();
            if (exp.envVariables != null) {
                for (String s : exp.envVariables) {
                    System.out.println(s);
                    String[] var = s.split("=", 2);
                    env.put(var[0], var[1]);
                }
            }
            //Set the experiment seed variable
            env.put("AUTOWEKA_EXPERIMENT_SEED", seed);

          

            Util.makePath(experiment.getParentFile() + File.separator + "out" + File.separator + "logs");
            Util.makePath(experiment.getParentFile() + File.separator + "out" + File.separator + "runstamps");

            File stampFile = new File(experiment.getParentFile() + File.separator + "out" + File.separator + "runstamps" + File.separator + seed + ".stamp");
            stampFile.createNewFile();
            stampFile.deleteOnExit();

            Process proc = pb.start();

            //Register a shutdown hook
            Runtime.getRuntime().addShutdownHook(new Util.ProcessKillerShutdownHook(proc));

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedWriter logOutput = new BufferedWriter(new FileWriter(experiment.getParentFile() + File.separator + "out" + File.separator + "logs" + File.separator + seed + ".log"));

            while ((line = reader.readLine()) != null) {
                if (!silent) {
                    System.out.println(line);
                }
                logOutput.write(line + "\n");
                logOutput.flush();
            }

            //And we might as well do the trajectory parse
            TrajectoryParser.main(new String[]{"-single", expFolder.getAbsolutePath(), seed});

            logOutput.close();

            if (!noExit) {
                System.exit(proc.waitFor());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Experiment createFromFolder(File folder) {
        File experiment = new File(folder.getAbsolutePath() + File.separator + folder.getName() + ".experiment");
        try {
            return Experiment.fromXML(new FileInputStream(experiment));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String prepareSMACCommand(Experiment exp){
        
        String smacCommand = "";
        
          String arg1 = "1024";

            File smacFolder = new File(exp.callString.get(0));
            String arg2 = smacFolder.getParent() + "\\";

            String arg3 = "";
            
            String smacLibFolderStrPath = arg2 +"lib\\";
            File smacLibFolder = new File(smacLibFolderStrPath);
            File[] listOfLibFiles = smacLibFolder.listFiles();

            for (int i = 0; i < listOfLibFiles.length; i++) {
                if (listOfLibFiles[i].isFile()) {
                    if (listOfLibFiles[i].getName().contains(".jar")) {
                        arg3+=smacLibFolderStrPath+listOfLibFiles[i].getName()+";";
                    }
                    
                    
                } 
            }

            String arg4 = "ca.ubc.cs.beta.smac.executors.SMACExecutor";

            String arg5 = "";
            for (int i = 1; i < exp.callString.size(); i++) {
                arg5 += exp.callString.get(i) + " ";
            }
            arg5 = arg5.trim();

            LoggerUtil.logAvatar("INIT SMAC");
            LoggerUtil.logAvatar("Arg1: " + arg1);
            LoggerUtil.logAvatar("Arg2: " + arg2);
            LoggerUtil.logAvatar("Arg3: " + arg3);
            LoggerUtil.logAvatar("Arg4: " + arg4);
            LoggerUtil.logAvatar("Arg5: " + arg5);
            
            
            
            smacCommand = "java -Xmx"+arg1+"m -cp \""+arg2+"conf\\;"+arg2+"patches\\;"+arg3+""+arg2+"patches\\ \" ca.ubc.cs.beta.aeatk.ant.execscript.Launcher "+arg4+" "+arg5;
        return smacCommand;
    }
}
