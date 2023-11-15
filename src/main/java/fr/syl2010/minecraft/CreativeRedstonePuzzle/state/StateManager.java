package fr.syl2010.minecraft.CreativeRedstonePuzzle.state;

import java.util.Objects;
import org.bukkit.Bukkit;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;

public class StateManager {

  private State currentState;

  public StateManager(State startingState) {
    currentState = Objects.requireNonNull(startingState);
    currentState.onEnter();
  }

  /**
   * Change the current state of the plugin
   * 
   * @param newState The new state of the plugin
   */
  public void setState(State newState) {
    Objects.requireNonNull(newState);
    currentState.onExit();

    fireStateChangeEvent(currentState, newState);
    currentState = newState;

    newState.onEnter();

    CreativeRedstonePuzzlePlugin.getPlugin().getLogger().info(String.format("Switch to %s state", currentState.getClass().getSimpleName()));
  }

  private void fireStateChangeEvent(State oldState, State newState) {
    Bukkit.getPluginManager().callEvent(new StateChangeEvent(oldState, newState));
  }

  public State getCurrentState() {
    return currentState;
  }

  public boolean isCurrentState(Class<? extends State> stateClass) {
    return stateClass.isAssignableFrom(currentState.getClass());
  }

}
