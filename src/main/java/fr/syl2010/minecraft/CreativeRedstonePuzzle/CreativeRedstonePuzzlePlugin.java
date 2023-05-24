package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import org.bukkit.plugin.java.JavaPlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.PuzzleManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.DisableState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LoadingState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LobbyState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.TeamManager;

public class CreativeRedstonePuzzlePlugin extends JavaPlugin {

  private StateManager stateManager;

  private PermissionManager permissionManager;
  private TeamManager       teamManager;
  private PuzzleManager     puzzleManager;

  @Override
  public void onLoad() {
    permissionManager = new PermissionManager();
    puzzleManager = new PuzzleManager();
    stateManager = new StateManager(new LoadingState());
  }

  @Override
  public void onEnable() {
    stateManager.setState(new LobbyState());
  }

  @Override
  public void onDisable() {
    stateManager.setState(new DisableState());
  }

  public PermissionManager getPermissionManager() {
    return permissionManager;
  }

  public TeamManager getTeamManager() {
    return teamManager;
  }

  public PuzzleManager getPuzzleManager() {
    return puzzleManager;
  }

  public static CreativeRedstonePuzzlePlugin getPlugin() {
    return getPlugin(CreativeRedstonePuzzlePlugin.class);
  }

}
