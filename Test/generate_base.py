import os
import random
from random import randrange

nodesFrom = []

n = 7

for i in range(n):
    nodesFrom.append(i)

nodes = open("France.nodes.csv", "w")
nodes.write("identifiant,x,y\n")

edges = open("France.edges.csv", "w")
edges.write("identifiant,from,to\n")

for i in range(n):

    f = nodesFrom[i]

    # Save the current node
    nodes.write("{},{},{}\n".format(f,f,f))

    if i < n-1:
        
        # To
        t = nodesFrom[i+1]

        # Edge
        edges.write("{},{},{}\n".format("{}{}".format(f,t),f,t))

nodes.close()
edges.close()
