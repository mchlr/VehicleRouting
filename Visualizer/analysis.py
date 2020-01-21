
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
from statistics import mean






dirname = "../output/analyse"
problist = os.listdir(dirname)        
#jsonfiles = os.listdir("../output/analyse/"+str(problist))

probdict = {}
bestvaldict={}
plotdict={}
plotpdict={}



def getProb(prob):
    global bestvaldict
    print(str(prob))
    prob=prob[:prob.index("-")].split('/')[-1]
  #  print(prob)
    dirname = os.listdir("../data")
    for probs in dirname:
        if probs[0:len(probs)-5] == prob:
            jsonfile="../data" + "/"+ probs
            with open(jsonfile, 'r') as f:
                data = f.read()
                jsondata = js.loads(data)
                bestvaldict.update({str(prob):jsondata["Optimal_value"]})
    







for probs in problist:
    #print(probs+":")
    getProb(probs)
    jsonfiles = "../output/analyse/"+str(probs)

   

    for json in os.listdir(jsonfiles):
     
        jsonfile=jsonfiles + "/"+ json
        if jsonfile:
            with open(jsonfile, 'r') as f:
                data=f.read()
                jsondata = js.loads(data)
                bestindex=int(jsondata['bestGenerationIndex'])
                probdict.setdefault(probs[:probs.index("-")].split('/')[-1],[])
                probdict[probs[:probs.index("-")].split('/')[-1]].append(int(jsondata['allGenerations'][bestindex]['allCosts'][0]))
    lst = probdict[probs[:probs.index("-")].split('/')[-1]]
    plotdict.update({probs[:probs.index("-")].split('/')[-1]:int(sum(lst)/len(lst))})
  


meanper=0
mean=0

print(probdict)

optlist =list(bestvaldict.values())
plotlist=list(plotdict.values())

print(len(plotpdict))
for x in range(len(plotdict)):
    mean += (optlist[x] / plotlist[x]) / len(plotdict)
    meanper += (abs((plotlist[x] - optlist[x]))/ optlist[x])/ len(plotdict)



# print(mean)
# print(meanper)


#print(probdict)
#print(range(len(bestvaldict)))
#print(range(len(probdict)))

#print(probdict)
#print(bestvaldict)

#print(plotdict)









#print(probdict.values())

y=list(plotdict.values())
y2=list(bestvaldict.values())




plt.subplot(211) 
plt.title('Accuracy at: '+ str(round(mean*100,2))+' %')

plt.scatter(plotdict.keys(), y,label="Mean current cost")
plt.scatter(bestvaldict.keys(), y2,label="Optimal cost",color="red")




z = range(len(bestvaldict))
i=0
for opt in y2:
    plt.annotate(opt, (z[i], y2[i]), ha='left')
    i+=1
t=0
for currentMean in y:
    plt.annotate(currentMean, (z[t], y[t]), ha='right')
    t+=1

plt.ylabel('Costs')
plt.xlabel('Tested Problems')

plt.grid(True)
plt.legend()
plt.draw()





  
data_to_plot = []
for t in probdict.keys():
    data_to_plot.append(probdict.get(t))







plt.subplot(212)

z = range(1,len(bestvaldict)+1)

i=0
for opt in y2:
    plt.annotate(opt, (z[i], y2[i]), ha='left')
    i+=1

plt.ylabel('Costs')
plt.xlabel('Tested Problems')

plt.title('Precision at: '+ str(round((1-meanper)*100,2))+' %')
plt.scatter(range(1,len(probdict)+1), y2,label="Optimal cost",color="red")
plt.boxplot(data_to_plot)
plt.xticks(range(1,len(probdict)+1), probdict.keys())
plt.grid(True)
plt.legend()
plt.show()

