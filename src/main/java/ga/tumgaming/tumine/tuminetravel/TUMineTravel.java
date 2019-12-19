package ga.tumgaming.tumine.tuminetravel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import ga.tumgaming.tumine.tuminetravel.listeners.TravelCommand;
import ga.tumgaming.tumine.tuminetravel.util.*;

public class TUMineTravel extends JavaPlugin {

	private Config travelPoints;
	private Config playerKnownPoints;
	private static Plugin plugin;
	private TravelManager travelManager;
	@Override
	public void onEnable() {
		TUMineTravel.plugin = this;

		playerKnownPoints = new Config(plugin, "playerKnownPoints");
		travelPoints = new Config(plugin, "travelPoints");
		
		travelManager = new TravelManager(playerKnownPoints, travelPoints);
		
	    // Register our command "travel" (set an instance of your command class as executor)
	    this.getCommand("travel").setExecutor(new TravelCommand(travelManager));
		
		registerEvents();

		log("Plugin erfolgreich geladen");
	}

	/**
	 * logs a String in the console
	 *
	 * @param str logged String
	 */
	public void log(String str) {
		Logger.getLogger(str);
	}

	private static void registerEvents() {
		PluginManager pluginManager = Bukkit.getPluginManager();
	}
}