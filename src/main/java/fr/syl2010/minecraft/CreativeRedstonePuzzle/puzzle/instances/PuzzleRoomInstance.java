package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import com.google.common.collect.Sets;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;

public class PuzzleRoomInstance {

  private final PuzzleRoom puzzleRoom;
  private final Location   playerSpanwpoint;

  private Set<String>             completedSteps  = new HashSet<>();
  private Map<String, Set<Block>> triggeredBlocks = new HashMap<>();
  private boolean                 isCompleted     = false;

  public PuzzleRoomInstance(PuzzleRoom puzzleRoom, Location roomOrigin) {
    this.puzzleRoom = Objects.requireNonNull(puzzleRoom);

    playerSpanwpoint = roomOrigin.clone().add(puzzleRoom.getStructure().getSize().divide(new Vector(2, 2, 2)));
    playerSpanwpoint.setY(playerSpanwpoint.getWorld().getHighestBlockYAt(playerSpanwpoint.getBlockX(), playerSpanwpoint.getBlockZ()) + 1);
  }

  public Location getPlayerSpawnpoint() {
    return playerSpanwpoint.clone();
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

  public Set<String> getSteps() {
    return puzzleRoom.getSteps();
  }

  public boolean completeRoom() {
    if (isCompleted)
      return false;
    else
      return isCompleted = true;
  }

  public StepResult triggerStep(Block commandBlock, String step) {
    if (!puzzleRoom.hasStep(step))
      return StepResult.UNKNOWN_STEP;
    else if (isCompleted)
      return StepResult.ALREADY_COMPLETED_ROOM;

    triggeredBlocks.computeIfAbsent(step, __ -> Sets.newHashSet()).add(commandBlock);
    if (commandBlock.isBlockPowered()) return completeStep(step);
    else {
      completedSteps.remove(step);
      return StepResult.NOT_POWERED_STEP;
    }
  }

  private boolean checkAllBlockSteps() {
    MutableBoolean result = new MutableBoolean(true);

    triggeredBlocks.forEach((step, blocks) -> {
      for (Block block : blocks) {
        if (block.isBlockPowered()) {
          completedSteps.add(step);
          return;
        }
      }
      completedSteps.remove(step);
      result.setValue(false);
    });
    return result.booleanValue();
  }

  public StepResult completeStep(String step) {
    if (!puzzleRoom.hasStep(step))
      return StepResult.UNKNOWN_STEP;
    else if (isCompleted)
      return StepResult.ALREADY_COMPLETED_ROOM;
    else if (!completedSteps.add(step))
      return StepResult.ALREADY_COMPLETED_STEP;

    checkAllBlockSteps();
    if (completedSteps.size() < puzzleRoom.getStepCount())
      return StepResult.COMPLETED_STEP;
    else {
      isCompleted = true;
      return StepResult.COMPLETED_ROOM;
    }
  }

  public static enum StepResult {
    UNKNOWN_STEP, NOT_POWERED_STEP, ALREADY_COMPLETED_STEP, COMPLETED_STEP, ALREADY_COMPLETED_ROOM, COMPLETED_ROOM;
  }
}
