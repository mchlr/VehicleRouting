import numpy as np
import matplotlib.pyplot as plt
import random

coords = [
    [6823, 4674],
    [7692, 2247],
    [9135, 6748],
    [7721, 3451],
    [8304, 8580],
    [7501, 5899],
    [4687, 1373]
]

tours = [
    [0, 6, 4, 3, 2, 0, 1, 5],
    [0, 6, 1, 5, 3, 0, 2, 4],
    [0, 5, 3, 4, 2, 0, 6, 1]
]

coordX = []
coordY = []
for node in coords:
    coordX.append(node[0])
    coordY.append(node[1])


toursX = []
toursY = []
for tour in tours:
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



colors = [
    "red",
    "green",
    "blue",
]

for i in range(len(toursX)-1):
    print("#" + str(i) + str(toursX[i]) + "/" + str(toursY[i]))
    ax.plot(toursX[i], toursY[i], color=colors[i], label="Tour #" + str(i))

#ax.plot(tourX, tourY, color='salmon', label="Tour")
ax.set_xlabel("x")
ax.set_ylabel("y")
ax.grid(True)
ax.legend(loc = 'best')

plt.show()
