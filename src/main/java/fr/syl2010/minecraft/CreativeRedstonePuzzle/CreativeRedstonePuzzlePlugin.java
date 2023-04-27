package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import org.bukkit.plugin.java.JavaPlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.DisableState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LoadingState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LobbyState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.TeamManager;

public class CreativeRedstonePuzzlePlugin extends JavaPlugin {

  public StateManager stateManager;

  public PermissionManager permissionManager;
  public TeamManager       teamManager;

  @Override
  public void onLoad() {
    permissionManager = new PermissionManager();
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

}
