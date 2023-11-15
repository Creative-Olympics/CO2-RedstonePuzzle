package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.bukkit.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.RoadmapInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class PuzzleManager {

  private static final ObjectMapper MAPPER = CreativeRedstonePuzzlePlugin.createDefaultMapper();

  private final File saveFile;

  private Roadmap                        roadmap;
  private Map<GameTeam, RoadmapInstance> runningMaps;

  public PuzzleManager() {
    runningMaps = new HashMap<>();

    saveFile = new File(CreativeRedstonePuzzlePlugin.getPlugin().getDataFolder(), "puzzles.json");
    if (!loadFile()) {
      roadmap = new Roadmap();
    }
    saveInFile();
  }

  // FIXME test Roadmap loading and saving
  private boolean loadFile() {
    if (saveFile.exists()) {
      try {
        roadmap = MAPPER.readValue(saveFile, Roadmap.class);
      } catch (IOException e) {
        throw new RuntimeException("Error while loading puzzle datas", e);
      }

      return true;
    } else return false;
  }

  private CompletableFuture<Void> saveInFile() {
    return CompletableFuture.runAsync(() -> {
      try {
        MAPPER.writeValue(saveFile, roadmap);
      } catch (IOException e) {
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().log(Level.SEVERE, "Error while saving puzzle datas", e);
        throw new RuntimeException("Error while saving puzzle datas", e);
      }
    });
  }

  @Nullable
  public Roadmap modifyRoadmap(Consumer<Roadmap.Spec> roadmapModifier) {
    roadmapModifier.accept(roadmap.new Spec());
    saveInFile();
    return roadmap;
  }

  @Nullable
  public Roadmap getRoadmap() {
    return roadmap;
  }

  public void clearRoadmap() {
    roadmap.clear();
    saveInFile();
  }

  public boolean isRoadmapValid() {
    if (roadmap.getRoomCount() == 0)
      return false;
    else
      return true;
  }

  public CompletableFuture<Map<GameTeam, RoadmapInstance>> generateMaps(Collection<GameTeam> teams) {
    return CompletableFuture.supplyAsync(() -> {

      if (!runningMaps.isEmpty()) {
        // destroying old worlds
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info("Destroying old game worlds...");
        for (RoadmapInstance runningMap : runningMaps.values()) {
          World world = runningMap.getWorld();
          if (world != null) {
            CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().deleteWorld(world);
          }
        }
      }

      runningMaps = new HashMap<>();
      // creating the new worlds

      if (teams.isEmpty()) throw new IllegalArgumentException("Can't generate maps if no team exist");
      CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info("Creating new game worlds...");
      for (GameTeam team : teams) {
        runningMaps.put(team, new RoadmapInstance(roadmap, team));
      }

      return Collections.unmodifiableMap(runningMaps);
    });
  }

  public boolean isEnded() {
    boolean ended = false;

    for (RoadmapInstance map : runningMaps.values()) {
      if (map.isCompleted()) {
        ended = true;
      } else return false;
    }
    return ended;
  }

  public RoadmapInstance getRunningRoadmap(GameTeam team) {
    return runningMaps.get(team);
  }

  public RoadmapInstance getRunningRoadmap(World world) {
    return getRunningRoadmap(CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().getTeam(world));
  }

}
