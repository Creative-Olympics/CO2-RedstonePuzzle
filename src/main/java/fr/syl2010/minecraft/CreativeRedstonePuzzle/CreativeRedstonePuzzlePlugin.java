package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import org.bukkit.plugin.java.JavaPlugin;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.PuzzleManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.RoomManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.serialisation.JsonMapKeyDeserializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.DisableState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LoadingState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.LobbyState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.TeamManager;

public class CreativeRedstonePuzzlePlugin extends JavaPlugin {

  private StateManager stateManager;

  private PermissionManager permissionManager;
  private TeamManager       teamManager;
  private RoomManager       roomManager;
  private PuzzleManager     puzzleManager;

  @Override
  public void onLoad() {
    stateManager = new StateManager(new LoadingState());
    permissionManager = new PermissionManager();
    teamManager = new TeamManager();
    roomManager = new RoomManager();
    puzzleManager = new PuzzleManager();
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

  public RoomManager getRoomManager() {
    return roomManager;
  }

  public PuzzleManager getPuzzleManager() {
    return puzzleManager;
  }

  public static ObjectMapper createDefaultMapper() {
    return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
      .registerModule(new SimpleModule().setDeserializerModifier(new BeanDeserializerModifier() {
        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDescription,
                                                      JsonDeserializer<?> originalDeserializer) {
          return new JsonMapKeyDeserializer(originalDeserializer, beanDescription);
        }
      }));
  }

  public static CreativeRedstonePuzzlePlugin getPlugin() {
    return getPlugin(CreativeRedstonePuzzlePlugin.class);
  }

}
