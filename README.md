# H-BRS DataScience Project WS19/20 - Ant Colony Optimization for CVRP's

## Informations

This is the official Repository for work related to the H-BRS's DataScience Project from WS19/20 by Tobias Krieger and Michael Renn.

## Presentation

Presentation done via LaTeX in Overleaf

*<Add Link here>

## Visualization

Visualization is done by a separate python program. The tool can be found within the /Visualizer/ folder.

The desired goal is to be able to visualize routes (e.g. the best one(s)) for each "generation" of ants.
Ideally, one should be able to step through the visualized generations by using matplotlib's widgets
*[Informations](https://riptutorial.com/matplotlib/example/23577/interactive-controls-with-matplotlib-widgets) - Widget Example

Currently all values are coded into the .py file. To launch it, adjust the values and then do:
```
cd Visualizer
python3 visualize.py
```

## TODOs


### Pheromon-Matrix


The Matrix should be scaled down/cut by one entire column and one row. The column, that is going to be cut, is phero[0] since this column contains the pheromone concentration for ways towards the depot. However, this is not neccessary since the depot is being visited before the capacity constraint would get violated.


#### Fix the pheroDeposition()-Method
This method has to get modified according to the change of the phero-Matrix described above.
*Nodes taken from the generated route has to be reduced by -1 in order to achieve correct matrix indices. However, this assumes, that the 0 nodes (depot) within a route have to be ignored.

### Visualizer



### Heuristics
Finish implementation of various heuristics:

* n-Opt
* Random Swaps

