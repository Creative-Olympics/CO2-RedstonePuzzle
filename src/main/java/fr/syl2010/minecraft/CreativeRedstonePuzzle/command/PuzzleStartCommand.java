package fr.syl2010.minecraft.CreativeRedstonePuzzle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import cloud.commandframework.annotations.CommandMethod;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LobbyState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.PlayingState;

public class PuzzleStartCommand {

  private final StateManager stateManager;

  public PuzzleStartCommand(StateManager stateManager) {
    this.stateManager = stateManager;
  }

  @CommandMethod("puzzle start")
  public void start(CommandSender sender) {
    if (!stateManager.isCurrentState(LobbyState.class)) {
      sender.sendMessage("§cCan't start a game out of the lobby state");
      return;
    } else if (!CreativeRedstonePuzzlePlugin.getPlugin().getPuzzleManager().isRoadmapValid()) {
      sender.sendMessage("§cCan't start a game without defining the parkour");
      return;
    } else if (CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager().getTeamCount() == 0) {
      sender.sendMessage("§cCan't start a game without an existing team");
      return;
    }

    Bukkit.getScheduler().runTask(CreativeRedstonePuzzlePlugin.getPlugin(), () -> stateManager.setState(new PlayingState()));
  }

}
