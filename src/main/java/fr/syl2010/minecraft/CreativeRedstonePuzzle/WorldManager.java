package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.ChunkGenerator;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class WorldManager implements Listener {

  private final Set<String>          expectedWorlds = new HashSet<>();
  private final Map<World, GameTeam> teamByWorld    = new HashMap<World, GameTeam>();

  public WorldManager(CreativeRedstonePuzzlePlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  public World generateTeamWorld(GameTeam team) {
    String worldName = String.format("%s_map", team.getId());

    if (!expectedWorlds.add(worldName)) {
      throw new IllegalArgumentException(String.format("This world is already about to generate : %s", worldName));
    }

    World world = Bukkit.createWorld(WorldCreator.name(worldName)
      .environment(Environment.CUSTOM)
      .generateStructures(false)
      .type(WorldType.FLAT)
      .generator(new ChunkGenerator() {}));

    teamByWorld.put(world, team);

    return world;
  }

  public void deleteWorld(World world) {
    if (!Bukkit.unloadWorld(world, false) || !world.getWorldFolder().delete()) {
      throw new RuntimeException(String.format("Unable to delete a team world : %s", world.getName()));
    }

    teamByWorld.remove(world);
  }

  public GameTeam getTeam(World world) {
    return teamByWorld.get(world);
  }

  @EventHandler
  public void onWorldInit(WorldInitEvent event) {
    if (expectedWorlds.remove(event.getWorld().getName())) {
      World world = event.getWorld();
      world.setKeepSpawnInMemory(false);
      world.setSpawnLocation(0, 0, 0);

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

}
