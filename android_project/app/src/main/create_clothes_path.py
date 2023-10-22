import os

root_path = "./assets/clothes"
tempera = os.listdir(root_path)

f = open("clothes_path.txt", 'w')

for temp in tempera:
    close = os.listdir(os.path.join(root_path,temp))
    for clo in close:
        dre = "clothes/"+temp+"/"+clo+"\n"
        f.write(dre)

f.close()

