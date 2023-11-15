package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.command.PuzzleResetCommand;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.command.PuzzleStartCommand;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.command.PuzzleStepCommand;
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
  private WorldManager      worldManager;
  private TeamManager       teamManager;
  private RoomManager       roomManager;
  private PuzzleManager     puzzleManager;

  private BukkitCommandManager<CommandSender> bukkitCommandManager;
  private AnnotationParser<CommandSender>     commandRegisterer;

  @Override
  public void onLoad() {
    getDataFolder().mkdirs();
    stateManager = new StateManager(new LoadingState());
    permissionManager = new PermissionManager();
    teamManager = new TeamManager();
    roomManager = new RoomManager();
    puzzleManager = new PuzzleManager();
  }

  @Override
  public void onEnable() {
    worldManager = new WorldManager(this);
    worldManager.setupLobbyWorld();

    try {
      bukkitCommandManager = new BukkitCommandManager<>(this,
        AsynchronousCommandExecutionCoordinator.<CommandSender>builder().withAsynchronousParsing().build(),
        Function.identity(), Function.identity());
      bukkitCommandManager.registerBrigadier();
      commandRegisterer = new AnnotationParser<>(bukkitCommandManager, CommandSender.class, __ -> SimpleCommandMeta.empty());
      setupCommands();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    stateManager.setState(new LobbyState());

    // FIXME puzzle test POC
    setupSimplePuzzle();
  }

  private void setupCommands() {
    registerCommand(new PuzzleStepCommand(stateManager, puzzleManager, worldManager));
    registerCommand(new PuzzleStartCommand(stateManager));
    registerCommand(new PuzzleResetCommand(stateManager));
  }

  // FIXME puzzle test POC
  private void setupSimplePuzzle() {
    resetSetup();
    teamManager.createTeam("test", "test", ChatColor.DARK_PURPLE);
    teamManager.addMember("test", UUID.fromString("c795c729-a406-417a-a718-e886bf37c801"));
    puzzleManager.modifyRoadmap(
      roadmap -> {
        roadmap.addRoom(roomManager.createRoom("test1", NamespacedKey.fromString("creative_redstone:structures/puzzle_1")));
        roadmap.addRoom(roomManager.createRoom("test2", NamespacedKey.fromString("creative_redstone:structures/puzzle_2")));
        roadmap.addRoom(roomManager.createRoom("test3", NamespacedKey.fromString("creative_redstone:structures/puzzle_3")));
        roadmap.addRoom(roomManager.createRoom("test4", NamespacedKey.fromString("creative_redstone:structures/puzzle_4")));
        roadmap.addRoom(roomManager.createRoom("test5", NamespacedKey.fromString("creative_redstone:structures/puzzle_5")));
        roadmap.addRoom(roomManager.createRoom("test6", NamespacedKey.fromString("creative_redstone:structures/puzzle_6")));
      });
  }

  // FIXME puzzle test POC
  private void resetSetup() {
    puzzleManager.clearRoadmap();
    roomManager.clearRooms();
    teamManager.clearTeams();
  }

  @Override
  public void onDisable() {
    stateManager.setState(new DisableState());
  }

  public void registerCommand(Object command) {
    commandRegisterer.parse(command);
  }

  public StateManager getStateManager() {
    return stateManager;
  }

  public PermissionManager getPermissionManager() {
    return permissionManager;
  }

  public WorldManager getWorldManager() {
    return worldManager;
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
      .registerModule(
        new SimpleModule()
          .setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDescription,
                                                          JsonDeserializer<?> originalDeserializer) {
              return new JsonMapKeyDeserializer(originalDeserializer, beanDescription);
            }
          })
          .addSerializer(NamespacedKey.class, new ToStringSerializer())
          .addDeserializer(NamespacedKey.class, new FromStringDeserializer<>(NamespacedKey.class) {

            private static final long serialVersionUID = 4899153976485799552L;

            @Override
            protected NamespacedKey _deserialize(String value, DeserializationContext ctxt) throws IOException {
              return NamespacedKey.fromString(value);
            }
          }));
  }

  public static CreativeRedstonePuzzlePlugin getPlugin() {
    return getPlugin(CreativeRedstonePuzzlePlugin.class);
  }

}
