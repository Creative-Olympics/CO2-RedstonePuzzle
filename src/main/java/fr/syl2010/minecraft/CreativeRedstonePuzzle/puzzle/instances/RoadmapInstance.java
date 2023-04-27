package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances;

import java.util.Objects;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.map.Roadmap;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class RoadmapInstance {

  private final Roadmap roadmap;

  private int                currentRoomIndex = -1;
  private PuzzleRoomInstance currentRoom      = null;

  public RoadmapInstance(Roadmap roadmap) {
    this.roadmap = Objects.requireNonNull(roadmap);
  }

  public GameTeam getTeam() {
    return roadmap.getTeam();
  }

  public PuzzleRoomInstance getCurrentRoom() {
    return currentRoom;
  }

  public PuzzleRoomInstance nextRoom() {
    if (++currentRoomIndex >= getRoomCount()) {
      return currentRoom = null;
    } else
      return currentRoom = roadmap.getRoom(currentRoomIndex).createInstance();
  }

  public int getCurrentRoomIndex() {
    return currentRoomIndex;
  }

  public int getRoomCount() {
    return roadmap.getRoomCount();
  }

  public boolean isCompleted() {
    return currentRoomIndex >= getRoomCount();
  }

}
