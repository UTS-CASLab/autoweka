package weka.filters;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import weka.core.Option;
import weka.core.Utils;

public class CategorizedMultiFilter extends MultiFilter {

	/** for serialization */
	private static final long serialVersionUID = -990500449629457741L;

	/** The filters */

	protected Filter m_BalancingData = new AllFilter();
	protected Filter m_MissingValuesHandling = new AllFilter();
	// weka.filters.unsupervised.attribute.ReplaceMissingValues
	protected Filter m_OutlierHandling = new AllFilter();
	// weka.filters.unsupervised.attribute.InterquartileRange
	// weka.filters.unsupervised.instance.RemoveWithValues
	protected Filter m_Transformation = new AllFilter();
	// weka.filters.unsupervised.attribute.Center
	// weka.filters.unsupervised.attribute.Standardize
	// weka.filters.unsupervised.attribute.Normalize
	protected Filter m_DimensionalityReduction = new AllFilter();
	// weka.filters.unsupervised.attribute.PrincipalComponents
	// weka.filters.unsupervised.attribute.RandomSubset
	// weka.filters.supervised.attribute.AttributeSelection
	protected Filter m_Sampling = new AllFilter();

	// weka.filters.unsupervised.instance.Resample
	// weka.filters.unsupervised.instance.ReservoirSample
	
	protected String combinationOrder = new String();

	/**
	 * Returns a string describing this filter
	 * 
	 * @return a description of the filter suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	@Override
	public String globalInfo() {
		return "Applies several filters successively. In case all supplied filters "
				+ "are StreamableFilters, it will act as a streamable one, too.";
	}

	/**
	 * Returns an enumeration describing the available options.
	 * 
	 * @return an enumeration of all the available options.
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> result = new Vector<Option>();
		result.addElement(new Option("\tBalancing filter.", "B", 1,
				"-B <classname [options]>"));

		result.addElement(new Option("\tMissing values filter.", "M", 1,
				"-M <classname [options]>"));

		result.addElement(new Option("\tOutlier handling filter.", "O", 1,
				"-O <classname [options]>"));

		result.addElement(new Option("\tTransformation filter.", "T", 1,
				"-T <classname [options]>"));

		result.addElement(new Option("\tDimensionality reduction filter.", "R",
				1, "-R <classname [options]>"));

		result.addElement(new Option("\tSampling filter.", "S", 1,
				"-S <classname [options]>"));
		
		
		result.addElement(new Option("\tCombination order of filters.", "C", 1,
				"-C <classname [options]>"));

		// result.addAll(Collections.list(super.listOptions()));

		return result.elements();
	}

	/**
	 * Parses a list of options for this object.
	 * <p/>
	 * 
	 * <!-- options-start --> Valid options are:
	 * <p/>
	 * 
	 * <pre>
	 * -D
	 *  Turns on output of debugging information.
	 * </pre>
	 * 
	 * <pre>
	 * -F &lt;classname [options]&gt;
	 *  A filter to apply (can be specified multiple times).
	 * </pre>
	 * 
	 * <!-- options-end -->
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @throws Exception
	 *             if an option is not supported
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		String filter;
		String[] options2;

		super.setOptions(options);

		// Balancing filters.
		String balancingDataOption = Utils.getOption("B", options);
		if (balancingDataOption.length() > 0) {
			options2 = Utils.splitOptions(balancingDataOption);
			filter = options2[0];
			options2[0] = "";
			m_BalancingData = (Filter) Utils.forName(Filter.class,
					filter, options2);
		} else {
			m_BalancingData = new AllFilter();
		}

		// MissingValues
		String missingValuesOption = Utils.getOption("M", options);
		if (missingValuesOption.length() > 0) {
			options2 = Utils.splitOptions(missingValuesOption);
			filter = options2[0];
			options2[0] = "";
			m_MissingValuesHandling = (Filter) Utils.forName(Filter.class,
					filter, options2);
		} else {
			m_MissingValuesHandling = new AllFilter();
		}

		// OutlierHandling
		String outlierHandlingOption = Utils.getOption("O", options);
		if (outlierHandlingOption.length() > 0) {
			options2 = Utils.splitOptions(outlierHandlingOption);
			filter = options2[0];
			options2[0] = "";
			m_OutlierHandling = (Filter) Utils.forName(Filter.class, filter,
					options2);
		} else {
			m_OutlierHandling = new AllFilter();
		}

		// Transformation
		String transformationOption = Utils.getOption("T", options);
		if (transformationOption.length() > 0) {
			options2 = Utils.splitOptions(transformationOption);
			filter = options2[0];
			options2[0] = "";
			m_Transformation = (Filter) Utils.forName(Filter.class, filter,
					options2);
		} else {
			m_Transformation = new AllFilter();
		}

		// DimensionalityReduction
		String dimensionalityReductionOption = Utils.getOption("R", options);
		if (dimensionalityReductionOption.length() > 0) {
			options2 = Utils.splitOptions(dimensionalityReductionOption);
			filter = options2[0];
			options2[0] = "";
			m_DimensionalityReduction = (Filter) Utils.forName(Filter.class,
					filter, options2);
		} else {
			m_DimensionalityReduction = new AllFilter();
		}

		// Sampling
		String samplingOption = Utils.getOption("S", options);
		if (samplingOption.length() > 0) {
			options2 = Utils.splitOptions(samplingOption);
			filter = options2[0];
			options2[0] = "";
			m_Sampling = (Filter) Utils.forName(Filter.class, filter, options2);
		} else {
			m_Sampling = new AllFilter();
		}
		
		
		// Combine filters by specified order
		String combineFiltersOption = Utils.getOption("C", options);
		if (combineFiltersOption.length() > 0) {
			combinationOrder = combineFiltersOption;
		} else {
			combinationOrder = "BMOTRS";
		}

		// Connect all filters
		updateFilters();

	}

	/**
	 * Gets the current settings of the filter.
	 * 
	 * @return an array of strings suitable for passing to setOptions
	 */
	@Override
	public String[] getOptions() {
		Vector<String> result;
		// String[] options;
		// int i;

		result = new Vector<String>();

		// options from parent
		// options = super.getOptions();
		// for (i = 0; i < options.length; i++) {
		// result.add(options[i]);
		// }

		result.add("-B");
		result.add(getFilterSpec(m_BalancingData));

		result.add("-M");
		result.add(getFilterSpec(m_MissingValuesHandling));

		result.add("-O");
		result.add(getFilterSpec(m_OutlierHandling));

		result.add("-T");
		result.add(getFilterSpec(m_Transformation));

		result.add("-R");
		result.add(getFilterSpec(m_DimensionalityReduction));

		result.add("-S");
		result.add(getFilterSpec(m_Sampling));
		
		result.add("-C");
		result.add("Combination Order of Filters");
	

		return result.toArray(new String[result.size()]);
	}

	private void updateFilters() {
		
		Filter[] flow = { 
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(0))), 
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(1))), 
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(2))),  
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(3))),  
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(4))),  
				getFilterFromCombinationOrder(Character.toString(combinationOrder.charAt(5)))  };
		
		//Filter[] flow = { m_BalancingData, m_MissingValuesHandling, m_OutlierHandling, m_Transformation, m_DimensionalityReduction, m_Sampling };
		//shuffleArray(flow);
		//Filter[] flow = { m_BalancingData};
		setFilters(flow);
	}
	
	private Filter getFilterFromCombinationOrder(String orderOption) {
		
		if (orderOption.equals("B")) 
			return m_BalancingData;
		else if (orderOption.equals("M")) 
			return m_MissingValuesHandling;
		else if (orderOption.equals("O")) 
			return m_OutlierHandling;
		else if (orderOption.equals("T")) 
			return m_Transformation;
		else if (orderOption.equals("R")) 
			return m_DimensionalityReduction;
		else if (orderOption.equals("S")) 
			return m_Sampling;
		else return null;
		
		
	}
	
	private void shuffleArray(Filter[] array)
	{
	    int index;
	    Filter temp;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = array[index];
	        array[index] = array[i];
	        array[i] = temp;
	    }
	}
	


	public String balancingDataTipText() {
		return "Balancing filter";
	}

	public Filter getBalancingData() {
		return m_BalancingData;
	}

	public void setBalancingData(Filter m_BalancingData) {
		this.m_BalancingData = m_BalancingData;
		updateFilters();
	}

	public String missingValuesTipText() {
		return "Missing values filter";
	}

	public Filter getMissingValuesHandling() {
		return m_MissingValuesHandling;
	}

	public void setMissingValuesHandling(Filter m_MissingValuesHandling) {
		this.m_MissingValuesHandling = m_MissingValuesHandling;
		updateFilters();
	}

	public String outlierHandlingTipText() {
		return "Outlier handling filter";
	}

	public Filter getOutlierHandling() {
		return m_OutlierHandling;
	}

	public void setOutlierHandling(Filter m_OutlierHandling) {
		this.m_OutlierHandling = m_OutlierHandling;
		updateFilters();
	}

	public String transformationTipText() {
		return "Transformation filter";
	}

	public Filter getTransformation() {
		return m_Transformation;
	}

	public void setTransformation(Filter m_Transformation) {
		this.m_Transformation = m_Transformation;
		updateFilters();
	}

	public String dimensionalityReductionTipText() {
		return "Dimensionality reduction filter";
	}

	public Filter getDimensionalityReduction() {
		return m_DimensionalityReduction;
	}

	public void setDimensionalityReduction(Filter m_DimensionalityReduction) {
		this.m_DimensionalityReduction = m_DimensionalityReduction;
		updateFilters();
	}
	
	public String samplingTipText() {
		return "Sampling filter";
	}

	public Filter getSampling() {
		return m_Sampling;
	}

	public void setSampling(Filter m_Sampling) {
		this.m_Sampling = m_Sampling;
		updateFilters();
	}
	
	public String getCombinationOrder() {
		return combinationOrder;
	}
	
	public void setCombinationOrder(String combinationOrder) {
		this.combinationOrder = combinationOrder;
		updateFilters();
	}
	

	/**
	 * Main method for executing this class.
	 * 
	 * @param args
	 *            should contain arguments for the filter: use -h for help
	 */
	public static void main(String[] args) {
		runFilter(new CategorizedMultiFilter(), args);
	}

}
