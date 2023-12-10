package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.Utils;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.RoadmapInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;
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

  public roadmapCheckResult isRoadmapValid() {
    if (roadmap.getRoomCount() == 0)
      return roadmapCheckResult.EMPTY;
    else {
      for (PuzzleRoom room : roadmap.getRooms()) {
        if (room.getSteps().isEmpty()) return roadmapCheckResult.MISSING_STEP;
      }
      return roadmapCheckResult.SUCCESS;
    }
  }

  public static enum roadmapCheckResult {
    SUCCESS, EMPTY, MISSING_STEP;
  }

  public CompletableFuture<Void> deleteWorlds() {
    return CompletableFuture.runAsync(() -> {
      if (!runningMaps.isEmpty()) {
        // destroying old world
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info("Destroying old game world...");
        CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().deleteGameWorld();
        runningMaps = new HashMap<>();
      }
    });
  }

  public CompletableFuture<Map<GameTeam, RoadmapInstance>> generateMaps(Collection<GameTeam> teams) {
    return deleteWorlds().thenApplyAsync(__ -> {
      // creating the new world
      if (teams.isEmpty()) throw new IllegalArgumentException("Can't generate maps if no team exist");
      CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info("Creating new game world...");
      World world = CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().generateGameWorld();

      CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info("Generating puzzles...");
      try {
        MutableInt index = new MutableInt(0);
        if (!Utils.iterateThroughTicks(teams,
          team -> runningMaps.put(team, new RoadmapInstance(roadmap, team, world, index.getAndIncrement())),
          10, TimeUnit.SECONDS, CreativeRedstonePuzzlePlugin.getPlugin())) {
          runningMaps.clear();
          throw new RuntimeException("A puzzle took too much time to generate");
        }
      } catch (InterruptedException e) {
        runningMaps.clear();
        throw new RuntimeException(e);
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

}
