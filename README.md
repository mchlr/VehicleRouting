# H-BRS DataScience Project WS19/20 - Ant Colony Optimization for CVRP's

## Informations

This is the official Repository for work related to the H-BRS's DataScience Project from WS19/20 by Tobias Krieger and Michael Renn. Supervised by Prof. Dr. Peter Becker.

## Presentation

Presentation done via LaTeX in Overleaf

* (Add Link here)

## Visualization

Visualization is done by a separate python programs. The tools can be found within the /Visualizer/ folder.

The desired goal is to be able to visualize routes (e.g. the best one(s)) for each "generation" of ants.

There a few files, named: visualize.py visualizer.py and analysis.py.
visualize.py shows you only the plot by adding coordinates and tours manuelly by hand.

The visualizer.py visualize routes, pheromones and give you a brief overlock over the generation of ants.
Furthermore you can use the buttons to step throught the generations , jump on the best generation with the best value or load new data.
There are also a Pythonskript, called analysis.py to calculate the accurray, and give you a plot about tested problems

## How does it work
It is important to start the those Python scrips within the /Visualizer/ folder.

The Reason for that is, analysis.py and visulizer.py looking in the /data/ folder and catch there some informations from the problem instance json.

If you start the scripts from another location , the are unable to find the /data/ folder and then may it works not correctly.

Another point is those scripts only work with the json files that were be created by our version of the Ant Colony Optimiziation Java program.

And some infromation about analysis.py.

Create a subfolder named "analysis" within /output/. Then copy the solved problem directories into /analysis/. Beware only one directory for each problem. So yes you have to copy for each problem all json files into their problem directory. 


```
cd Visualizer
python3 visualize.py
```

## TODOs

* Add the output of the best route per generation as a file
* Check for convergence

### Pheromon-Matrix

#### General
The Matrix should be scaled down/cut by one entire column and one row. The column, that is going to be cut, is phero[0] since this column contains the pheromone concentration for ways towards the depot. However, this is not neccessary since the depot is being visited before the capacity constraint would get violated.

* ~~Add a return to the depot (0) after all nodes have been visited.~~ DONE!

#### Fix the pheroDeposition()-Method
This method has to get modified according to the change of the phero-Matrix described above.

**Info**: Everything related to this issue is marked with "TODO - ArrayFix" within the source code.

* Nodes taken from the generated route have to be reduced by -1 in order to achieve correct matrix indices. However, this assumes that 0 nodes within a route are being ignored
* Consequently, if a route looks like X -> 0 -> Y the pheromons from X -> Y have to get updated. Its necessary to write an updated logic to tackle this problem

### Visualizer

* Add persicion on analysis.py plot

### Heuristics
Finish implementation of various heuristics:
* n-Opt
* Random Swaps

