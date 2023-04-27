package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances;

import java.util.Objects;
import org.bukkit.Location;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.map.PuzzleRoom;

public class PuzzleRoomInstance {
  
  private final PuzzleRoom puzzleRoom;
  
  private boolean isCompleted = false;
  
  public PuzzleRoomInstance(PuzzleRoom puzzleRoom) {
    this.puzzleRoom = Objects.requireNonNull(puzzleRoom);
  }
  
  public Location getPlayerSpawnpoint() {
    return puzzleRoom.getPlayerSpawnpoint();
  }
  
  public boolean isCompleted() {
    return isCompleted;
  }
  
  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

}
