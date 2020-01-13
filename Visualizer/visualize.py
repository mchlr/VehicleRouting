import numpy as np
import matplotlib.pyplot as plt
import random
import json
import glob




coords = [
        [69 ,63],
        [3 ,35],
        [71 ,79],
        [1 ,47],
        [11 ,15],
        [87 ,23],
        [37 ,33],
        [87 ,29],
        [35 ,81],
        [55 ,71],
        [41 ,51],
        [93 ,9],
        [11 ,49],
        [75 ,89],
        [75 ,69],
        [97 ,95],
        [15 ,13],
        [63 ,95],
        [47 ,41],
        [45 ,41],
        [89 ,43],
        [45 ,59],
        [95 ,23],
        [19 ,83],
        [71 ,69],
        [27 ,19],
        [17 ,57],
        [93 ,15],
        [59 ,29],
        [35 ,39],
        [33 ,51],
        [61 ,21],
        [89 ,53],
        [33 ,85],
        [37 ,37],
        [21 ,91],
        [67 ,95],
        [61 ,15]
]

#tourFiles = glob.glob("../output/*.json")
#print(tourFiles)
#exit()

tours = [
    #[0,2,4,5,0,3,1,6,0], # best tour generated
    #[0,6,5,4,0,3,1,2,0], # worst tour generated
    #[0,1,2,3,4,25,10,5,6,29,42,7,27,8,9,11,0,12,13,14,15,16,17,26,19,36,18,35,20,21,22,23,0,24,28,30,37,31,32,33,34,38,39,40,41,44,43,45,0,46,47,0]
    #[0,1,2,3,4,25,10,24,5,6,29,7,18,35,27,8,0,9,37,11,12,13,14,15,16,17,26,42,19,36,20,21,0,22,23,28,30,43,45,31,32,33,34,38,39,40,41,44,0,46,47,0]
    #[0,1,2,3,4,25,10,5,6,7,8,9,37,11,12,13,0],[0,14,15,16,17,26,42,19,36,18,35,27,20,21,22,23,0],[0,24,28,29,30,43,45,31,32,33,34,38,39,40,41,44,0],[0,46,47,0]
    #[0,13,21,46,20,6,36,19,26,42,17,29,32,45,15,12,0],[0,8,11,23,3,39,1,9,8,37,30,43,18,7,27,35,14,0],[0,33,40,16,22,2,25,4,34,44,10,24,41,47,5,28,0],[0,31,38,0]
     [0,37, 11, 27, 22, 5, 7,0],[0,10,30,29,34,19,18,0],[0,20,32,15,13,36,17,2,14,0],[0,28,31,6,25,16,4,1,3,12,26,21,0],[0,24,33,35,23,8,9,0]


]
def plotour(tours,coords):
    coordX = []
    coordY = []
    for node in coords:
        coordX.append(node[0])
        coordY.append(node[1])


    toursX = []
    toursY = []
    for tour in tours:
        print("LenaTOurs")
        print(tour)
        tourNodesX = []
        tourNodesY = []

        for node in tour:
            tourNodesX.append(coords[node][0])
            tourNodesY.append(coords[node][1])

        toursX.append(tourNodesX)
        toursY.append(tourNodesY)



    fig, ax = plt.subplots(figsize=(5, 5))
    ax.scatter(coords[0][0], coords[0][1], s=200, color='green', label="Depot")

    for x in range(1, len(coords)):
        ax.scatter(coords[x][0],coords[x][1], color="gold")
        ax.annotate(str("#" + str(x)), (coords[x][0],coords[x][1]))

    # Print out Informations about the routes into the console;
    for tour in tours: 
        print(tour)


    for i in range(len(toursX)):
        print("#" + str(i) + str(toursX[i]) + "/" + str(toursY[i]))
        ax.plot(toursX[i], toursY[i], color=np.random.rand(3,), label="Tour #" + str(i))


    ax.set_xlabel("x")
    ax.set_ylabel("y")
    ax.grid(True)
    ax.legend(loc = 'best')

    plt.show()


plotour(tours,coords)