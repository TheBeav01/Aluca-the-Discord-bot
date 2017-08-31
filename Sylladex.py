from random import randint
item = raw_input("Enter item name: ")
cards = input("Enter number of cards: ")
slot = ((pow(len(item*randint(1,cards)),(randint(1,25)))) // cards) + randint(1,25)
if slot > cards:
    fix = slot % cards
    print "Slot: ", fix
elif fix == 0:
     fix = randint(1,slot)
     print "Slot: ", fix
elif slot > cards:
    slot // cards
    print "Slot: ", fix
elif slot == 0:
    slot + randint(1,cards-1)
else:
    print slot
