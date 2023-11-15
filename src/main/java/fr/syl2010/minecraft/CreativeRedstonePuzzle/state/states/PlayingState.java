package fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states;

import java.util.ListIterator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.PuzzleRoomInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.RoadmapInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.State;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class PlayingState implements State, Listener {

  @Override
  public void onEnter() {
    Bukkit.getPluginManager().registerEvents(this, CreativeRedstonePuzzlePlugin.getPlugin());

    CreativeRedstonePuzzlePlugin.getPlugin()
      .getPuzzleManager()
      .generateMaps(CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager().getTeams().values())
      .thenAccept(runningMaps -> {
        // TODO re-sync teleport thread?
        Bukkit.broadcastMessage("§aTeleporting...");
        runningMaps.forEach((team, map) -> map.teleportToNextRoom());
        Bukkit.broadcastMessage("§aGame started!");
      });
    Bukkit.broadcastMessage("§aGenerating Map...");
  }

  private static Location getPlayerSpawnLocation(Player player) {
    GameTeam team = CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager().getTeamOf(player.getUniqueId());

    if (team != null) {
      RoadmapInstance map = CreativeRedstonePuzzlePlugin.getPlugin().getPuzzleManager().getRunningRoadmap(team);
      if (map != null) {
        PuzzleRoomInstance room = map.getCurrentRoom();
        if (room != null) return room.getPlayerSpawnpoint();
      } else {
        CreativeRedstonePuzzlePlugin.getPlugin()
          .getLogger()
          .warning(String.format("The team %s doesn't have a running map", team.getName()));
      }
    }

    return CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().getLobbyWorld().getSpawnLocation();
  }

  @EventHandler
  void onJoinLocation(PlayerSpawnLocationEvent event) {
    event.setSpawnLocation(getPlayerSpawnLocation(event.getPlayer()));
  }

  @EventHandler
  void onJoin(PlayerJoinEvent event) {
    if (!CreativeRedstonePuzzlePlugin.getPlugin().getPermissionManager().isOperator(event.getPlayer())) {
      event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
  }

  @EventHandler
  void onDeath(PlayerDeathEvent event) {
    event.setKeepInventory(true);
    event.setKeepLevel(true);
  }

  @EventHandler
  void onRespawn(PlayerRespawnEvent event) {
    event.setRespawnLocation(getPlayerSpawnLocation(event.getPlayer()));
  }

  @EventHandler
  void onLeave(PlayerQuitEvent event) {
    World world = event.getPlayer().getWorld();
    ListIterator<ItemStack> iterator = event.getPlayer().getInventory().iterator();

    while (iterator.hasNext()) {
      world.dropItem(event.getPlayer().getLocation(), iterator.next());
      iterator.remove();
    }
  }

  @Override
  public void onExit() {
    HandlerList.unregisterAll(this);
  }
}
