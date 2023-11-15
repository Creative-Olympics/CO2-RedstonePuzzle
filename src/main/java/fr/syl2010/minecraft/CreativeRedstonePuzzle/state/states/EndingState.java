package fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.State;

public class EndingState implements State {

  @Override
  public void onEnter() {
    Location lobbyLocation = CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().getLobbyWorld().getSpawnLocation();
    Bukkit.getOnlinePlayers().forEach(player -> player.teleport(lobbyLocation));
    Bukkit.broadcastMessage("§2§lGame ended!");
  }

  @Override
  public void onExit() {}

}
