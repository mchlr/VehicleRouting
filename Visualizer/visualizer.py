
import tkinter as tk
import tkinter.filedialog
from tkinter.filedialog import askopenfilenames
import os
import json as js
import numpy as np
import matplotlib.pyplot as plt
import ast
import scipy
from matplotlib.widgets import Button
import seaborn as sns
import random
import matplotlib.gridspec as gridspec
from scipy.stats import linregress





coord =[]
tourCost = []
alltours = []
bestGenerationIndex = 0
probabilityMatrix = []
X = []
best = 0;


plt.figure(figsize=(80, 80))

plt.subplot(2,2,1)
plt.title('Pheromones')
plt.subplot(2,2,2)
plt.title('Tourplot')
plt.subplot(2,2,(3,4))
plt.title('Average Tourcost')

plt.subplots_adjust(bottom=0.2)

plt.ioff()

class Index(object):
    ind = 0

    def next(self, event):
        self.ind += 1
        i = self.ind % len(probabilityMatrix)

        plt.subplot(2,2,1)
        plt.cla()
        plt.imshow(probabilityMatrix
        [self.ind], cmap="Blues", interpolation='nearest')
        plt.title('Phermones Gen_'+str(self.ind))
        plt.draw()

        Index.plotTour([alltours[self.ind]],coord,self.ind)


    def prev(self, event):
        self.ind -= 1
        i = self.ind % len(probabilityMatrix)
        plt.subplot(2,2,1)
        plt.cla()
        plt.imshow(probabilityMatrix[self.ind], cmap="Blues", interpolation='nearest')
        plt.title('Phermones Gen_'+str(self.ind))  
    
        Index.plotTour([alltours[self.ind]],coord,self.ind)

        plt.draw()



    def getProb(prob):
        global coord
        global best
        coord.clear()
        best=0
        dirname = os.listdir("../data")
        for probs in dirname:
            if probs[0:len(probs)-5] == prob:
                #print(True)
                jsonfile="../data" + "/"+ probs
                with open(jsonfile, 'r') as f:
                    data = f.read()
                    jsondata = js.loads(data)
                    coord.append((jsondata["node_coordinates"]))
                    best = jsondata["Optimal_value"]
                    coord = coord[0]


    def plotTour(tours,coords,count):
        coordX = []
        coordY = []
      #  print("Anfang:" +str(coords))
            
        for node in coords:
            coordX.append(node[0])
            coordY.append(node[1])


        toursX = []
        toursY = []
        for tour in tours:
            # print(tour)
            tourNodesX = []
            tourNodesY = []

            for node in tour:
                tourNodesX.append(coords[node][0])
                tourNodesY.append(coords[node][1])

            toursX.append(tourNodesX)
            toursY.append(tourNodesY)



        plt.subplot(2,2,2)
        plt.cla()
        plt.scatter(coords[0][0], coords[0][1], s=200, color='green', label="Depot")

        for x in range(1, len(coords)):
            plt.scatter(coords[x][0],coords[x][1], color="gold")
            plt.annotate(str("#" + str(x)), (coords[x][0],coords[x][1]))


        for i in range(len(toursX)):
            # print("#" + str(i) + str(toursX[i]) + "/" + str(toursY[i]))
            plt.plot(toursX[i], toursY[i], color=np.random.rand(3,), label="Tour #" + str(i))

        plt.title('Tourplot Gen_'+str(count))
        plt.draw()


    def goto(self,event):
        plt.subplot(2,2,1)
        plt.cla()
        plt.imshow(probabilityMatrix[bestGenerationIndex], cmap="Blues", interpolation='nearest')
        plt.title('Phermones Gen_'+str(bestGenerationIndex))
        print(bestGenerationIndex)
        print(str([tourCost[bestGenerationIndex]]))
        Index.plotTour([alltours[bestGenerationIndex]],coord,bestGenerationIndex)
        plt.draw()

    def plotTourcost():
        X=range(len(tourCost))
        b, a, r, p, std = linregress(X,tourCost)
        plt.subplot(2,2,(3,4))
        plt.cla()
        plt.plot(X, tourCost, color="black")
        plt.plot([0,len(tourCost)],[a,a+len(tourCost)*b],c="red",alpha=0.5,lineWidth = 3)
        plt.scatter(bestGenerationIndex, min(tourCost), c="green",linewidths= 3)
        plt.vlines(x=bestGenerationIndex, ymin = 0, ymax = min(tourCost) ,color="green",linestyle='-')
        plt.hlines(min(tourCost),xmin=0, xmax= bestGenerationIndex,colors="green")
        plt.hlines(best,xmin=0, xmax=len(X), colors="orange")
        plt.title('Tourcost')

    def readjson(self, event):    
        global tourCost
        global X
        global bestGenerationIndex
        global alltours 
        global bestGenerationIndex 
        global probabilityMatrix 


        tourCost.clear()
        alltours.clear()
        probabilityMatrix.clear()

    

        dirname = tk.filedialog.askdirectory(initialdir="/",  title='Please select a directory')
        dirlist = os.listdir(dirname)        
        prob=dirname[:dirname.index("-")].split('/')[-1]
        plt.suptitle(str(prob), fontsize=16)
        Index.getProb(prob)

        
        i=0
        for json in dirlist:
  
            jsonfile=dirname + "/"+ json
            if json:
                with open(jsonfile, 'r') as f:
                    data = f.read()
                    jsondata = js.loads(data)      
                    bestGenerationIndex = int(jsondata['bestGenerationIndex'])
                    for generation in jsondata['allGenerations']:
                        tourCost.append(jsondata['allGenerations'][i]['allCosts'][0])      
                        alltours.append(jsondata['allGenerations'][i]['allTours'][0])  
                        probabilityMatrix.append(jsondata['allGenerations'][i]['probabilityMatrix'])
                        i += 1
            
        #Plot Pheros
        plt.subplot(2,2,1)
        plt.imshow(probabilityMatrix[0], cmap="Blues", interpolation='nearest')
        plt.title('Pheromones Gen_0')


        #Plot Tourcost
        Index.plotTourcost()

        #Plot Graph
        Index.plotTour([alltours[0]],coord,0)

        plt.draw()
        

  
        
                




    
                



    

callback = Index()
axprev = plt.axes([0.7, 0.05, 0.1, 0.075])
axnext = plt.axes([0.81, 0.05, 0.1, 0.075])
axsel = plt.axes([0.5, 0.05, 0.1, 0.075])
axgoto = plt.axes([0.2, 0.05, 0.1, 0.075])
bnext = Button(axnext, 'Next')
bnext.on_clicked(callback.next)
bprev = Button(axprev, 'Previous')
bprev.on_clicked(callback.prev)
bsel = Button(axsel,'Select Data')
bsel.on_clicked(callback.readjson)
bgoto = Button(axgoto,'Goto Minimum')
bgoto.on_clicked(callback.goto)

        

plt.show()