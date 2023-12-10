package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.ChunkGenerator;

public class WorldManager implements Listener {

  private final Set<String> expectedWorlds = new HashSet<>();
  private World             gameWorld;

  public WorldManager(CreativeRedstonePuzzlePlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  public World generateGameWorld() {
    String worldName = String.format("game_world");

    if (!expectedWorlds.add(worldName))
      throw new IllegalArgumentException(String.format("This world is already about to generate : %s", worldName));

    gameWorld = Bukkit.createWorld(WorldCreator.name(worldName)
      .environment(Environment.NORMAL)
      .generateStructures(false)
      .type(WorldType.FLAT)
      .generator(new ChunkGenerator() {}));

    return gameWorld;
  }

  public boolean deleteGameWorld() {
    if (gameWorld != null) {
      deleteWorld(gameWorld);
      return true;
    } else return false;
  }

  public void deleteWorld(World world) {
    if (!Bukkit.unloadWorld(world, false))
      throw new RuntimeException(String.format("Unable to unload a team world : %s", world.getName()));

    try {
      FileUtils.deleteDirectory(world.getWorldFolder());
    } catch (IOException e) {
      throw new RuntimeException(String.format("Unable to delete a team world : %s", world.getName()), e);
    }

    if (world == gameWorld) {
      gameWorld = null;
    }
  }

  @EventHandler
  public void onWorldInit(WorldInitEvent event) {
    if (expectedWorlds.remove(event.getWorld().getName())) {
      World world = event.getWorld();
      world.setKeepSpawnInMemory(false);
      world.setSpawnLocation(0, 0, 0);
      world.setPVP(false);

      world.setGameRule(GameRule.SPAWN_RADIUS, 0);
      world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
      world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
      world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
      world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
      world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
      world.setGameRule(GameRule.KEEP_INVENTORY, true);
      world.setGameRule(GameRule.MOB_GRIEFING, false);
      world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);

      world.setFullTime(6000);
    }
  }

  public World getLobbyWorld() {
    return Bukkit.getWorlds().get(0);
  }

  public World getGameWorld() {
    return gameWorld;
  }

  public void setupLobbyWorld() {
    World world = getLobbyWorld();
    world.setSpawnLocation(0, 0, 0);
    world.setPVP(false);

    world.setGameRule(GameRule.SPAWN_RADIUS, 0);
    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
    world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
    world.setGameRule(GameRule.KEEP_INVENTORY, true);
    world.setGameRule(GameRule.MOB_GRIEFING, false);
    world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);

    world.setFullTime(6000);

    // TODO generated platform
    for (int x = -10; x <= 10; ++x) {
      for (int z = -10; z <= 10; ++z) {
        world.setType(x, -1, z, Material.GLASS);
      }
    }
  }

}
