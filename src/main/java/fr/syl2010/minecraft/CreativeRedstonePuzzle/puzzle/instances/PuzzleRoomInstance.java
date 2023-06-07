package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Location;
import com.google.common.collect.Sets;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;

public class PuzzleRoomInstance {

  private final PuzzleRoom puzzleRoom;

  private Set<String> completedSteps = new HashSet<>();
  private boolean     isCompleted    = false;

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

  public Set<String> getCompletedSteps() {
    return Collections.unmodifiableSet(completedSteps);
  }

  public Set<String> getRemainingSteps() {
    return Sets.difference(puzzleRoom.getSteps(), completedSteps);
  }

  public boolean completeRoom() {
    if (isCompleted)
      return false;
    else
      return isCompleted = true;
  }

  public StepResult completeStep(String step) {
    if (!puzzleRoom.hasStep(step))
      return StepResult.UNKNOWN_STEP;
    else if (isCompleted)
      return StepResult.ALREADY_COMPLETED_ROOM;
    else if (!completedSteps.add(step))
      return StepResult.ALREADY_COMPLETED_STEP;
    else if (completedSteps.size() < puzzleRoom.getStepCount())
      return StepResult.COMPLETED_STEP;
    else {
      isCompleted = true;
      return StepResult.COMPLETED_ROOM;
    }
  }

  public static enum StepResult {
    UNKNOWN_STEP, ALREADY_COMPLETED_STEP, COMPLETED_STEP, ALREADY_COMPLETED_ROOM, COMPLETED_ROOM;
  }

}
