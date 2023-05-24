package fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team;

import java.util.Objects;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public abstract class TeamEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  public final GameTeam team;

  public TeamEvent(GameTeam team) {
    this.team = team;
  }

  public GameTeam getTeam() {
    return team;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public static class TeamCreateEvent extends TeamEvent {

    public TeamCreateEvent(GameTeam team) {
      super(team);
    }
  }

  public static class TeamRemoveEvent extends TeamEvent {

    public TeamRemoveEvent(GameTeam team) {
      super(team);
    }
  }

  public static class TeamChangeNameEvent extends TeamEvent implements Cancellable {

    private String  newName;
    private boolean isCancelled;

    public TeamChangeNameEvent(GameTeam team, String newName) {
      super(team);
      this.newName = Objects.requireNonNull(newName);
    }

    public String getOldName() {
      return team.getName();
    }

    public String getNewName() {
      return newName;
    }

    public void setNewName(String newName) {
      this.newName = Objects.requireNonNull(newName);
    }

    @Override
    public boolean isCancelled() {
      return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
      isCancelled = cancelled;
    }
  }

}
