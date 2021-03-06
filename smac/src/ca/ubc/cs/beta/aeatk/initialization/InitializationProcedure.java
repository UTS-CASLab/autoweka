package ca.ubc.cs.beta.aeatk.initialization;

import ca.ubc.cs.beta.aeatk.algorithmrunresult.AlgorithmRunResult;
import ca.ubc.cs.beta.aeatk.parameterconfigurationspace.ParameterConfiguration;
import java.util.List;

public interface InitializationProcedure extends Runnable {

    /**
     * After running this procedure postcondition: Incumbent has the most number
     * of runs in the run history object postcondition: Instance Seeds that
     * appear in Run History object will NOT be generated by instance seed
     * generator again
     *
     */
    public void run();

    /**
     *
     * @return incumbent selected
     */
    public ParameterConfiguration getIncumbent();
    
    public void setIncumbent(ParameterConfiguration incumbent);
    
    public List<AlgorithmRunResult> getListOfAlgorithmRunResults(); 

    

}
