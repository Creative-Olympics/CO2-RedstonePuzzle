package fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team;

import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.event.HandlerList;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public abstract class TeamMemberEvent extends TeamEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  private final UUID member;

  public TeamMemberEvent(GameTeam team, UUID member) {
    super(team);
    this.member = member;
  }

  public UUID getMember() {
    return member;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public static class TeamMemberJoinEvent extends TeamMemberEvent {

    private final GameTeam oldTeam;

    public TeamMemberJoinEvent(GameTeam team, UUID member) {
      this(team, member, null);
    }

    public TeamMemberJoinEvent(GameTeam team, UUID member, @Nullable GameTeam oldTeam) {
      super(team, member);
      this.oldTeam = oldTeam;
    }

    @Nullable
    public GameTeam getOldTeam() {
      return oldTeam;
    }
  }

  public static class TeamMemberLeaveEvent extends TeamMemberEvent {

    public TeamMemberLeaveEvent(GameTeam team, UUID member) {
      super(team, member);
    }
  }
}
