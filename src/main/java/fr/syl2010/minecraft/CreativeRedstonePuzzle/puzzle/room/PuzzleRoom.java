package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.structure.Structure;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.serialisation.JsonMapKey;

public class PuzzleRoom {

  @JsonMapKey
  private final String name;

  private NamespacedKey structurePath;

  private transient Structure structure;

  private final Set<String> steps;

  public PuzzleRoom(String name, NamespacedKey structurePath) {
    this.name = name;
    setStructurePath(structurePath);
    steps = new HashSet<String>();
  }

  private void setStructurePath(NamespacedKey structurePath) {
    structure = Objects.requireNonNull(Bukkit.getStructureManager().getStructure(structurePath), "Structure not found");
    this.structurePath = structurePath;
  }

  public String getName() {
    return name;
  }

  public NamespacedKey getStructurePath() {
    return structurePath;
  }

  public Structure getStructure() {
    return structure;
  }

  public Set<String> getSteps() {
    return Collections.unmodifiableSet(steps);
  }

  public boolean hasStep(String step) {
    return steps.contains(step);
  }

  public int getStepCount() {
    return steps.size();
  }

  public class Spec {
    Spec() {}

    public String getName() {
      return name;
    }

    public void setStructurePath(NamespacedKey newStructurePath) {
      setStructurePath(newStructurePath);
    }

    public NamespacedKey getStructurePath() {
      return structurePath;
    }

    public Set<String> getSteps() {
      return steps;
    }
  }
}
