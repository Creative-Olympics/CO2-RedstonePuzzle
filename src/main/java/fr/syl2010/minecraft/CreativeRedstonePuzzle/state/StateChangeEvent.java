package fr.syl2010.minecraft.CreativeRedstonePuzzle.state;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event thrown when the plugin change his current state. This event
 * is not thrown at the first state initialization
 * 
 * @author Syl2010
 */
public class StateChangeEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  public final State oldState;
  public final State newState;

  StateChangeEvent(State oldState, State newState) {
    this.oldState = oldState;
    this.newState = newState;
  }

  /**
   * @return Previous state, can't be null
   */
  public State getOldState() {
    return oldState;
  }

  /**
   * @return New state, can't be null
   */
  public State getNewState() {
    return newState;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
