package autoweka.ui;


import javax.swing.JFrame;

import org.javabuilders.swing.SwingJavaBuilder;
import uts.aai.utils.IOUtils;



import weka.gui.GUIChooser;

public class Launcher extends JFrame
{
    private static final long serialVersionUID = -3238517942590016822L;

    public static void main(String[] args)
    {
        Launcher l = new Launcher();
        l.setVisible(true);
//        String configDir = autoweka.Util.getAbsoluteClasspath();
//        configDir = configDir.substring(0, configDir.lastIndexOf('\\'));
//        
//        String smacConfig = "smacexecutable="+ configDir.replaceAll("\\\\", "/")+"/smac-v2.10.03-master-778/smac";
//        IOUtils iou = new IOUtils();
//        iou.overWriteData(smacConfig, configDir + "\\autoweka.smac.SMACExperimentConstructor.properties");
        
    }

    public Launcher()
    {
        SwingJavaBuilder.build(this);

    }

    public void wizardClicked()
    {
        Wizard wiz = new Wizard();
        wiz.setVisible(true);
    }

    public void builderClicked()
    {
        ExperimentBuilder builder = new ExperimentBuilder();
        builder.setVisible(true);
    }

    public void runnerClicked()
    {
        ExperimentRunner runner = new ExperimentRunner();
        runner.setVisible(true);
    }

    public void extractorClicked()
    {
        TrainedModelRunner runner = new TrainedModelRunner();
        runner.setVisible(true);
    }

    public void wekaClicked()
    {
        GUIChooser.main(new String[]{}); 
    }
};
