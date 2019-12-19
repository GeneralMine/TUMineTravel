package ga.tumgaming.tumine.tuminetravel;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ga.tumgaming.tumine.tuminetravel.util.Config;

public class TravelManager {
	private Config playerKnownPoints;
	private Config travelPoints;

	public TravelManager(Config playerKnownPoints, Config travelPoints) {
		this.playerKnownPoints = playerKnownPoints;
		this.travelPoints = travelPoints;
	}

	public void addPoint(String name, Location loc) {
		travelPoints.set(name,
				loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getWorld().getName());
	}

	public void deletePoint(String name) {
		travelPoints.delete(name);
	}

	public Location getPoint(String name) {
		String s = travelPoints.get(name);
		String[] sloc = s.split(",");
		World w = Bukkit.getWorld(sloc[3]);
		Location loc = new Location(w, Integer.parseInt(sloc[0]), Integer.parseInt(sloc[1]), Integer.parseInt(sloc[2]));
		return loc;
	}
	
	public String[] getAllPoints() {
		String[] allKeys = travelPoints.getAllKeys();
		String[] allPoints = new String[allKeys.length];
		
		
		for(int i = 0; i < allKeys.length; i++) {
			allPoints[i] = allKeys[i] + ":" + (String) travelPoints.get(allKeys[i]);
		}
		
		return allPoints;
	}

	public void addPlayerKnownPoint(String name, UUID uuid) {
		String s = playerKnownPoints.get(uuid.toString());
		if (s == null || s.equals("")) {
			playerKnownPoints.set(uuid.toString(), name);
		} else {
			s = s + "," + name;
			playerKnownPoints.set(uuid.toString(), s);
		}
	}

	public void removePlayerKnownPoint(String name, UUID uuid) {
		String s = playerKnownPoints.get(uuid.toString());
		String[] points = s.split(",");
		String newPoint;
		int index = 0;
		if (points[0].equals(name)) {
			index++;
			newPoint = points[index];
		} else {
			newPoint = points[index];
		}
		for (; index < points.length; index++) {
			if (!points[index].equals(name)) {
				newPoint = newPoint + "," + points[index];
			}
		}
	}

	public boolean playerKnowsPoint(String name, UUID uuid) {
		String s = playerKnownPoints.get(uuid.toString());
		if(s != null) {
			String[] points = s.split(",");
			for (int i = 0; i < points.length; i++) {
				if (points[i].equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public String[] getPlayerKnownPoints(UUID uuid) {
		if (playerKnownPoints.get(uuid.toString()) != null) {
			String s = playerKnownPoints.get(uuid.toString());
			String[] sloc = s.split(",");
			return sloc;
		}
		return null;
	}

	public void givePlayerPointMap(String pointName, UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (!(playerKnowsPoint(pointName, player.getUniqueId()))) {
			addPlayerKnownPoint(pointName, uuid);
			player.sendMessage("Wow your an adventurer! You visited " + pointName + "!");
			player.getInventory().addItem(generatePointMap(pointName));
		}
	}
	
	public ItemStack generatePointMap(String pointName) {
		ItemStack currentPointMap = new ItemStack(Material.PAPER);
		ItemMeta meta = currentPointMap.getItemMeta();
		meta.setDisplayName(pointName);
		meta.setLore(Arrays.asList("You have visited " + pointName + " before!",
				"This is the directions to " + pointName + ".", "Use this to tell a travel guide where to go!"));
		currentPointMap.setItemMeta(meta);
		return currentPointMap;
	}
	
	public void teleportPlayerToPoint(UUID uuid, String pointName) {
		// TODO money check
		
		Player player = Bukkit.getPlayer(uuid);
		
		if(playerKnowsPoint(pointName, uuid)) {
			String s = travelPoints.get(pointName);
			String[] sloc = s.split(",");
			Location loc = new Location(Bukkit.getWorld(sloc[3]), Integer.parseInt(sloc[0]),Integer.parseInt(sloc[1]),Integer.parseInt(sloc[2]));
			
			player.teleport(loc);
		}
	}
}