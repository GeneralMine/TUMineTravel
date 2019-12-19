# TUMineTravel
## Functionality
A travel guide (Citizen NPC) calls ``/travel travel <player> <currentPoint>``
Player discovers point and teleports to destination point if he has a dircetion map in his hands
## Commands
- travel
	- add
		- <name>
		Creates a travel point with <name> at current commandSender position
	- remove/delete
		- <name>
		removes travel point with <name>
	- list
		lists all travel points
	- travel
	travel npc runs this command
		- <player>
			- <currentPoint>
			<player> gets to know the <currentPoint>
				- <destinationPoint>
				<player> teleports to <destinationPoint>
				<destinationPoint> is normally got from directionMap <player> hands in his hands