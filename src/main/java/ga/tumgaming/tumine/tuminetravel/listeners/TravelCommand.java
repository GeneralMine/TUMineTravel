package ga.tumgaming.tumine.tuminetravel.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ga.tumgaming.tumine.tuminetravel.TravelManager;

public class TravelCommand implements CommandExecutor {

	private TravelManager travelManager;

	public TravelCommand(TravelManager travelManager) {
		this.travelManager = travelManager;
	}

	@EventHandler
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player && args[0] != null) {
			Player commandSenderPlayer = (Player) commandSender;

			// args[0] => commandType
			switch (args[0]) {
			case "add": {
				// args[1] => newPointName
				if (args.length >= 2 && args[1] != null) {
					String newPointName = args[1];
					travelManager.addPoint(newPointName, commandSenderPlayer.getLocation());
					commandSenderPlayer.sendMessage("Point " + newPointName + " created successfully!");
				}
				break;
			}
			case "remove": {
				// args[1] => removePointName
				if (args.length >= 2 && args[1] != null) {
					String removePointName = args[1];
					travelManager.deletePoint(removePointName);
					commandSenderPlayer.sendMessage("Point " + removePointName + " removed successfully!");
				}
				break;
			}
			case "delete": { // same as remove
				// args[1] => removePointName
				if (args.length >= 2 && args[1] != null) {
					String removePointName = args[1];
					travelManager.deletePoint(removePointName);
					commandSenderPlayer.sendMessage("Point " + removePointName + " removed successfully!");
				}
				break;
			}
			case "list": {
				// no more arguments
				String[] allPoints = travelManager.getAllPoints();
				commandSenderPlayer.sendMessage("There are currently " + allPoints.length + " Points registered:");
				for (String point : allPoints) {
					commandSenderPlayer.sendMessage(point);
				}
				break;
			}
			case "travel": {
				// args[1] => playerName
				// args[2] => currentPoint
				// args[3] => destinationPoint, input via map name in hand of player
				if (args.length >= 3 && args[1] != null && args[2] != null) {
					Player player = Bukkit.getPlayer(args[1]);
					String currentPoint = args[2];

					// check if player knows currentPoint !=> give a map of this point
					if (!(travelManager.playerKnowsPoint(currentPoint, player.getUniqueId()))) {
						travelManager.givePlayerPointMap(currentPoint, player.getUniqueId());
					} else { // only for debugging
						player.sendMessage("Your already know " + currentPoint + "!");
					}

					// check if player wants to travel to a destination
					// check if there is a destination argument
					// or if there is a valid map of a known point in the hand of the player
					if ((args.length >= 4 && args[3] != null)
							|| (player.getInventory().getItemInMainHand().getType() == Material.PAPER
									&& travelManager.playerKnowsPoint(
											player.getInventory().getItemInMainHand().getItemMeta().getDisplayName(),
											player.getUniqueId())
									&& player.getInventory().getItemInMainHand()
											.isSimilar(travelManager.generatePointMap(player.getInventory()
													.getItemInMainHand().getItemMeta().getDisplayName())))) {
						// travel to destination
						String destinationPoint = (args.length >= 4 && args[3] != null) ? args[3]
								: player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
						if (!destinationPoint.equals(currentPoint)) {
							travelManager.teleportPlayerToPoint(player.getUniqueId(), destinationPoint);
							player.sendMessage("You traveled from " + currentPoint + " to " + destinationPoint + "!");
						} else {
							player.sendMessage("You cannot travel to your current position!");
						}
					}
				}
				break;
			}
			}

		}
		return false;
	}

}
