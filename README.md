# Auto-WEKA for MCPS (AutoWeka4MCPS)
Auto-WEKA is a tool for automating the selection of methods and hyperparameters of WEKA. This repository contains an extended version of Auto-WEKA now supporting the optimisation of MultiComponent Predictive Systems (MCPS).

![GUI Main](https://raw.githubusercontent.com/dsibournemouth/autoweka/master/img/GUI-main.png)

## Description
Many different machine learning algorithms exist that can easily be used off the shelf, many of these methods are implemented in the open source WEKA package. However, each of these algorithms have their own hyperparameters that can drastically change their performance, and there are a staggeringly large number of possible alternatives overall. Auto-WEKA considers the problem of simultaneously composing an MCPS and setting its hyperparameters, going beyond previous methods that address these issues in isolation. Auto-WEKA does this using a fully automated approach, leveraging recent innovations in Bayesian optimization. Our hope is that Auto-WEKA will help non-expert users to more effectively identify machine learning algorithms and hyperparameter settings appropriate to their applications, and hence to achieve improved performance.

## Auto-WEKA as a blackbox
Auto-WEKA includes a wizard to find the best MCPS of a given dataset without any user interaction, apart from providing a dataset and a time budget.
![GUI Wizard](https://raw.githubusercontent.com/dsibournemouth/autoweka/master/img/GUI-wizard.png)

## Auto-WEKA as an advanced toolbox
Auto-WEKA allows to select between a list of all WEKA methods and different optimisation strategies and parameters.
![GUI Builder](https://raw.githubusercontent.com/dsibournemouth/autoweka/master/img/GUI-builder.png)

## Search space reduction
This repository provides supplementary documents for a research journal article on search space reduction with the title: "On Taking Advantage of Opportunistic Meta-knowledge to Reduce Configuration Spaces for Automated Machine Learning" (https://github.com/UTS-CASLab/autoweka/tree/master/autoweka4mcps/doc/spacereduction/)

To reduce the search space of AutoWeka4MCPS, please select filters and predictors when creating experiments as mentioned in previous section. This script is created to run experiments on virtual machines (VMs). The experiment settings can be created and stored in cloud storage such as Dropbox or Google Drive. This script moves experiment settings to VMs, run experiments, make a zip file of experiment results and move them to the cloud storage.

```
// move the experiment folder from dropbox to the experiment folder of AutoWeka4MCPS
xcopy C:\..\add7_shuttle_seed0 C:\..\autoweka4mcps\experiments\add7_shuttle_seed0\ /E/H

// move the configuration file to the experiment folder of AutoWeka4MCPS
xcopy C:\Users\Administrator\Dropbox\add_configs\add7\add7_shuttle_seed0\listofexperiments.properties C:\experiments\tools\autoweka4mcps-a\autoweka4mcps\ /Y

// run experiments
cd C:\experiments\tools\autoweka4mcps-a\autoweka4mcps
java -Xmx2048m -cp autoweka4mcps.jar autoweka.command.App C:\..\autoweka4mcps\listofexperiments.properties

// zip experiments and move to dropbox
"C:\Program Files\7-Zip\7z.exe" a -r "C:\..\autoweka4mcps\experiments\add7_shuttle_seedx.zip" "C:\..\autoweka4mcps\experiments\*.*"
move "C:\..\autoweka4mcps\experiments\add7_shuttle_seedx.zip" "C:\..\Dropbox\add_configs_results\"
```

The mean error rate and structure of the best pipelines can be extracted using the following script:

"""
https://github.com/UTS-CASLab/autoweka/blob/master/autoweka4mcps/src/uts/landmarking/GenerateAnalysisReport.java
"""



Optimisation efficiency and the model accuracy attainable for a fixed time budget suffer if this pipeline configuration space is excessively large.
A key research question is whether it is both possible and practical to preemptively avoid costly evaluations of poorly performing ML pipelines by leveraging their historical performance for various ML tasks, i.e. meta-knowledge.
This research formulates the problem of configuration space reduction in the context of AutoML.
Given a pool of available ML components, it then investigates whether previous experience can recommend the most promising subset to use as a configuration space when initiating a pipeline composition/optimisation process for a new ML problem, i.e.~running AutoML on a new dataset.
Specifically, we conduct experiments to explore (1) what size the reduced search space should be and (2) which strategy to use when recommending the most promising subset.
The previous experience comes in the form of classifier/regressor accuracy rankings derived from either (1) a substantial but non-exhaustive number of pipeline evaluations made during historical AutoML runs, i.e. opportunistic meta-knowledge, or (2) comprehensive cross-validated evaluations of classifiers/regressors with default hyperparameters, i.e. systematic meta-knowledge.
Overall, numerous experiments with the AutoWeka4MCPS package, including ones leveraging similarities between datasets via the relative landmarking method, suggest that (1) opportunistic/systematic meta-knowledge can improve ML outcomes, typically in line with how relevant that meta-knowledge is, and (2) configuration-space culling is optimal when it is neither too conservative nor too radical.
However, the utility and impact of meta-knowledge depend critically on numerous facets of its generation and exploitation, warranting extensive analysis; these are often overlooked/underappreciated within AutoML and meta-learning literature.
In particular, we observe strong sensitivity to the challenge of a dataset, i.e. whether specificity in choosing a predictor leads to significantly better performance.
Ultimately, identifying difficult datasets, thus defined, is crucial to both generating informative meta-knowledge bases and understanding optimal search-space reduction strategies.


## Usage 

* Please watch the [video tutorial](https://github.com/UTS-AAi/autoweka/blob/master/autoweka4mcps/autoweka4mcps_quick_start_guide_ui.mp4) for quick start guide with the user interface (autoweka4mcps/autoweka4mcps_quick_start_guide_ui.mp4)
* It's neccessary to allocate more memory for AutoWeka4MCPS to deal with large datasets, e.g., allocating 2GB memory:
```
cd autoweka4mcps
java -Xmx2048m -jar autoweka4mcps.jar
```

* SMAC Configuration: Please configure the file path to SMAC in the following file autoweka.smac.SMACExperimentConstructor.properties

* The experiments folders have to be put in the parent folder of AutoWeka4MCPS (default)



## Publications
* Tien-Dung Nguyen, David Jacob Kedziora, Katarzyna Musial, and Bogdan Gabrys. ["Exploring Opportunistic Meta-knowledge to Reduce Search Spaces for Automated Machine Learning."](https://arxiv.org/pdf/2105.00282.pdf). In The International Joint Conference on Neural Network IJCNN. IEEE, 2021. (Accepted)
* David Jacob Kedziora, Katarzyna Musial, and Bogdan Gabrys. ["AutonoML: Towards an Integrated Framework for Autonomous Machine Learning."](https://arxiv.org/pdf/2012.12600) arXiv preprint arXiv:2012.12600 (2020). (Under Review)
* Tien-Dung Nguyen, Bogdan Gabrys, and Katarzyna Musial. ["AutoWeka4MCPS-AVATAR: Accelerating Automated Machine Learning Pipeline Composition and Optimisation."](https://arxiv.org/abs/2011.11846) arXiv preprint arXiv:2011.11846. 2020. (Under Review)
* Tien-Dung Nguyen, Tomasz Maszczyk, Katarzyna Musial, Marc-Andre Zöller, and Bogdan Gabrys. ["AVATAR-Machine Learning Pipeline Evaluation Using Surrogate Model"](https://link.springer.com/chapter/10.1007/978-3-030-44584-3_28). In International Symposium on Intelligent Data Analysis, pp. 352-365. Springer, Cham, 2020.
* Manuel Martin Salvador, Marcin Budka, and Bogdan Gabrys. "Automatic composition and optimisation of multicomponent predictive systems with an extended Auto-WEKA" Submitted to IEEE Transactions on Automation Science and Engineering, 2018. [[slides](http://www.slideshare.net/draxus/automating-machine-learning-is-it-feasible-62661182)] [[results](https://github.com/dsibournemouth/autoweka/tree/master/results)]
 * Manuel Martin Salvador, Marcin Budka, and Bogdan Gabrys. "Modelling Multi-Component Predictive Systems as Petri Nets". Submitted to 15th Annual Industrial Simulation Conference, 2017 (under review). [[branch](https://github.com/dsibournemouth/autoweka/tree/feature/pnml)]
 * Manuel Martin Salvador, Marcin Budka, and Bogdan Gabrys. ["Effects of change propagation resulting from adaptive preprocessing in multicomponent predictive systems"](http://www.sciencedirect.com/science/article/pii/S187705091632066X). In Proc. of the 20th International Conference KES-2016.
 * Manuel Martin Salvador, Marcin Budka, and Bogdan Gabrys. ["Adapting Multicomponent Predictive Systems using Hybrid Adaptation Strategies with Auto-WEKA in Process Industry"](http://www.jmlr.org/proceedings/papers/v64/salvador_adapting_2016.pdf). In Proc. of the 2016 Workshop on Automatic Machine Learning at ICML 2016. [[branch](https://github.com/dsibournemouth/autoweka/tree/feature/batch-adaptation)] [[results](https://github.com/dsibournemouth/autoweka/tree/feature/batch-adaptation/results)]  
 * Manuel Martin Salvador, Marcin Budka, and Bogdan Gabrys. ["Towards automatic composition of multicomponent predictive systems"](http://link.springer.com/chapter/10.1007%2F978-3-319-32034-2_3) In Proc. of HAIS 2016, 2016. [[slides](http://www.slideshare.net/draxus/towards-automatic-composition-of-multicomponent-predictive-systems)] [[results](https://github.com/dsibournemouth/autoweka/tree/master/results)]
 * Chris Thornton, Frank Hutter, Holger Hoos, and Kevin Leyton-Brown. ["Auto-WEKA: Combined Selection and Hyperparameter Optimization of Classifiaction Algorithms"](https://dl.acm.org/citation.cfm?id=2487629) In Proc. of KDD 2013, 2013.

# The Extension of Auto-WEKA for MCPS with the AVATAR
 * This version of Auto-WEKA is extended by integrating the AVATAR (https://github.com/UTS-AAi/AVATAR) . The AVATAR evaluates the validity of ML pipelines using a surrogate model that enables
to accelerate automatic ML pipeline composition and optimisation.

## Authors of this Auto-WEKA extension
 * [Tien-Dung Nguyen] (https://www.linkedin.com/in/tien-dung-nguyen-29bb42170/), University of Technology Sydney, Australia
 * [Professor Bogdan Gabrys] (http://bogdan-gabrys.com/), University of Technology Sydney, Australia
 * [Associate Professor Katarzyna Musial Gabrys] (http://www.katarzyna-musial.com/), University of Technology Sydney, Australia
 * [Tomasz.Maszczyk], University of Technology Sydney, Australia
 * [Marc-André Zöller] (https://www.researchgate.net/profile/Marc_Andre_Zoeller), USU Software AG, Germany
 * [Simon Kocbek] (https://www.uts.edu.au/staff/simon.kocbek), University of Technology Sydney, Australia

## Authors of the original Auto-Weka for MCPS
 * [Manuel Martin Salvador](http://staffprofiles.bournemouth.ac.uk/display/msalvador), PhD Candidate (Bournemouth University)
 * [Marcin Budka](http://staffprofiles.bournemouth.ac.uk/display/mbudka), Senior Lecturer (Bournemouth University)
 * [Bogdan Gabrys](http://bogdan-gabrys.com), Professor (Bournemouth University)
 
## Original authors of Auto-WEKA
 * [Chris Thornton](http://www.cs.ubc.ca/~cwthornt/), M.Sc. Student (UBC)
 * [Frank Hutter](http://www2.informatik.uni-freiburg.de/~hutter/index.html), Assistant Professor (Freiburg University)
 * [Holger Hoos](http://www.cs.ubc.ca/~hoos/), Professor (UBC)
 * [Kevin Leyton-Brown](http://www.cs.ubc.ca/~kevinlb/), Associate Professor (UBC)
 
## Notes
This version was developed using as base Auto-WEKA 0.5 from http://www.cs.ubc.ca/labs/beta/Projects/autoweka/

## Disclaimer
This software is intended for research purposes and not recommended for production environments. Support is not guaranteed, but please [contact us](https://github.com/dsibournemouth/autoweka/issues) if you have any question or would like to collaborate.

## License
GNU General Public License v3 (see [LICENSE](https://github.com/DraXus/autoweka/blob/master/LICENSE))
