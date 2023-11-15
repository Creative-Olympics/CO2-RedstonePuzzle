package fr.syl2010.minecraft.CreativeRedstonePuzzle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import cloud.commandframework.annotations.CommandMethod;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LoadingState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LobbyState;

public class PuzzleResetCommand {

  private final StateManager stateManager;

  public PuzzleResetCommand(StateManager stateManager) {
    this.stateManager = stateManager;
  }

  @CommandMethod("puzzle reset")
  public void reset(CommandSender sender) {
    if (stateManager.isCurrentState(LobbyState.class) || stateManager.isCurrentState(LoadingState.class)) {
      sender.sendMessage("§cCan't reset the game if it didn't start");
      return;
    }

    Bukkit.getScheduler().runTask(CreativeRedstonePuzzlePlugin.getPlugin(), () -> stateManager.setState(new LobbyState()));
  }

}
