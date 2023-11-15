package fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.State;

public class LobbyState implements State, Listener {

  private final Location lobbyLocation;

  public LobbyState() {
    lobbyLocation = CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().getLobbyWorld().getSpawnLocation();
  }

  @Override
  public void onEnter() {
    Bukkit.getPluginManager().registerEvents(this, CreativeRedstonePuzzlePlugin.getPlugin());
    Bukkit.getOnlinePlayers().forEach(player -> player.teleport(lobbyLocation));
  }

  @EventHandler
  void onJoin(PlayerJoinEvent event) {
    if (!CreativeRedstonePuzzlePlugin.getPlugin().getPermissionManager().isOperator(event.getPlayer())) {
      event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
  }

  @EventHandler
  void onJoinLocation(PlayerSpawnLocationEvent event) {
    event.setSpawnLocation(lobbyLocation);
  }

  @EventHandler
  void onRespawn(PlayerRespawnEvent event) {
    event.setRespawnLocation(lobbyLocation);
  }

  @EventHandler
  void onPlayerDamage(EntityDamageByEntityEvent event) {
    if (event.getEntityType() == EntityType.PLAYER) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  void onFoodChange(FoodLevelChangeEvent event) {
    event.setFoodLevel(20);
  }

  @Override
  public void onExit() {
    HandlerList.unregisterAll(this);
  }

}
