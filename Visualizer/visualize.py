import numpy as np
import matplotlib.pyplot as plt
import random
import json
import glob




coords = [
        [6823, 4674],
        [7692, 2247],
        [9135, 6748],
        [7721, 3451],
        [8304, 8580],
        [7501, 5899],
        [4687, 1373],
        [5429, 1408],
        [7877, 1716],
        [7260, 2083],
        [7096, 7869],
        [6539, 3513],
        [6272, 2992],
        [6471, 4275],
        [7110, 4369],
        [6462, 2634],
        [8476, 2874],
        [3961, 1370],
        [5555, 1519],
        [4422, 1249],
        [5584, 3081],
        [5776, 4498],
        [8035, 2880],
        [6963, 3782],
        [6336, 7348],
        [8139, 8306],
        [4326, 1426],
        [5164, 1440],
        [8389, 5804],
        [4639, 1629],
        [6344, 1436],
        [5840, 5736],
        [5972, 2555],
        [7947, 4373],
        [6929, 8958],
        [5366, 1733],
        [4550, 1219],
        [6901, 1589],
        [6316, 5497],
        [7010, 2710],
        [9005, 3996],
        [7576, 7065],
        [4246, 1701],
        [5906, 1472],
        [6469, 8971],
        [6152, 2174],
        [5887, 3796],
        [7203, 5958]
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
    [0,13,21,46,20,6,36,19,26,42,17,29,32,45,15,12,0],[0,8,11,23,3,39,1,9,8,37,30,43,18,7,27,35,14,0],[0,33,40,16,22,2,25,4,34,44,10,24,41,47,5,28,0],[0,31,38,0]
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