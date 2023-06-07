package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.annotation.Nullable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;

public class PuzzleManager {

  private static final ObjectMapper MAPPER = CreativeRedstonePuzzlePlugin.createDefaultMapper();

  private Map<String, Roadmap> roadmapMap;

  private final File saveFile;

  public PuzzleManager() {
    saveFile = new File(CreativeRedstonePuzzlePlugin.getPlugin().getDataFolder(), "puzzles.json");
    if (!loadFile()) {
      roadmapMap = new HashMap<>();
    }
    saveInFile();
  }

  // FIXME test Roadmap loading and saving
  private boolean loadFile() {
    if (saveFile.exists()) {
      try {
        roadmapMap = MAPPER.readValue(saveFile, new TypeReference<Map<String, Roadmap>>() {});
      } catch (IOException e) {
        throw new RuntimeException("Error while loading puzzle datas", e);
      }

      return true;
    } else {
      return false;
    }
  }

  private CompletableFuture<Void> saveInFile() {
    Map<String, Roadmap> savingMap = ImmutableMap.copyOf(roadmapMap);
    return CompletableFuture.runAsync(() -> {
      try {
        MAPPER.writeValue(saveFile, savingMap);
      } catch (IOException e) {
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().log(Level.SEVERE, "Error while saving puzzle datas", e);
        throw new RuntimeException("Error while saving puzzle datas", e);
      }
    });
  }

  public Roadmap createOrModifyRoadmap(String id, Consumer<Roadmap.Spec> roadmapModifier) {
    Roadmap roadmap = roadmapMap.computeIfAbsent(id, intId -> new Roadmap());
    roadmapModifier.accept(roadmap.new Spec());

    saveInFile();
    return roadmap;
  }

  @Nullable
  public Roadmap modifyRoadmap(String id, Consumer<Roadmap.Spec> roadmapModifier) {
    Roadmap roadmap = roadmapMap.get(id);
    if (roadmap == null)
      return null;
    else {
      roadmapModifier.accept(roadmap.new Spec());
      saveInFile();
      return roadmap;
    }
  }

  @Nullable
  public Roadmap getRoadmap(String id) {
    return roadmapMap.get(id);
  }

  public boolean removeRoadmap(String roadmapId) {
    if (roadmapMap.remove(roadmapId) != null) {
      saveInFile();
      return true;
    } else
      return false;
  }

  public void clearRoadmaps() {
    roadmapMap.clear();
    saveInFile();
  }

  public PuzzleCheckResult verifyRoadmaps() {
    if (roadmapMap.isEmpty())
      return PuzzleCheckResult.EMPTY_MAPS;

    int puzzleCount = -1;
    for (Roadmap roadmap : roadmapMap.values()) {
      if (roadmap.getTeam() == null)
        return PuzzleCheckResult.MISSING_TEAM;

      if (puzzleCount < 0)
        puzzleCount = roadmap.getRoomCount();
      else if (puzzleCount != roadmap.getRoomCount()) {
        return PuzzleCheckResult.UNBALANCED_MAPS;
      }
    }
    return PuzzleCheckResult.APPROVED;
  }

  public static enum PuzzleCheckResult {
    APPROVED, EMPTY_MAPS, UNBALANCED_MAPS, MISSING_TEAM;
  }

}
