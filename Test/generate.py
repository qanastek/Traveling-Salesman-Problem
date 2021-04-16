import os
import random

nodesFrom = []
nodesEnd = []

for rep in range(3):
    for i in range(500):
        nodesFrom.append(i)
        nodesEnd.append(i)

random.shuffle(nodesFrom)
random.shuffle(nodesFrom)

random.shuffle(nodesEnd)
random.shuffle(nodesEnd)

cpt = 0

used = []

ed = open("France.edges.csv", "w")
ed.write("identifiant,from,to\n")
while(cpt < 5):

    f = random.choice(nodesFrom)
    t = random.choice(nodesEnd)

    if f == t:
        continue

    used.append(f)
    used.append(t)

    nodesFrom.remove(f)
    nodesEnd.remove(t)

    ed.write("{},{},{}\n".format("{}{}".format(f,t),f,t))

    cpt += 1

ed.close()

used = list(set(used))
used.sort()

out = open("France.nodes.csv", "w")
out.write("identifiant,x,y\n")
for n in used:
    out.write("{},{},{}\n".format(n,n,n))
out.close()

print("Length of from: " + str(len(nodesFrom)))
print("Length of end: " + str(len(nodesEnd)))
print("Length of cpt: " + str(cpt))

