package fr.syl2010.minecraft.CreativeRedstonePuzzle.state;

import java.util.Objects;

import org.bukkit.Bukkit;

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
  }

  private void fireStateChangeEvent(State oldState, State newState) {
    Bukkit.getPluginManager().callEvent(new StateChangeEvent(oldState, newState));
  }

}
