package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    saveFile = new File(CreativeRedstonePuzzlePlugin.getPlugin().getDataFolder(), "puzzles.json");
    runningMaps = new HashMap<>();
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
    } else {
      return false;
    }
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
  public Roadmap modifyRoadmap(String id, Consumer<Roadmap.Spec> roadmapModifier) {
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

  public Map<GameTeam, RoadmapInstance> generateMaps(List<GameTeam> teams) {
    // destroying old worlds
    for (RoadmapInstance runningMap : runningMaps.values()) {
      World world = runningMap.getWorld();
      if (world != null) {
        CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().deleteWorld(world);
      }
    }

    runningMaps = new HashMap<>();
    // creating the new worlds
    for (GameTeam team : teams) {
      runningMaps.put(team, new RoadmapInstance(roadmap, team));
    }

    return Collections.unmodifiableMap(runningMaps);
  }

}
