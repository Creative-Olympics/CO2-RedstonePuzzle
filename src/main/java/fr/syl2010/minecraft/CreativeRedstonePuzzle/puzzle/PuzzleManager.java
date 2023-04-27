package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.util.HashMap;
import java.util.Map;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.map.Roadmap;

public class PuzzleManager {

  private final Map<String, Roadmap> roadmapMap = new HashMap<>();

  public Roadmap getOrCreateRoadmap(String id) {
    return roadmapMap.computeIfAbsent(id, intId -> new Roadmap());
  }

  public Roadmap getRoadmap(String roadmapId) {
    return roadmapMap.get(roadmapId);
  }

  public Roadmap removeRoadmap(String roadmapId) {
    return roadmapMap.remove(roadmapId);
  }

}
