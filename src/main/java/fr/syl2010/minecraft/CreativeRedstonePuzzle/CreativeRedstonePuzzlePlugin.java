package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.io.IOException;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.DisablingState;
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
    registerCommand(new PuzzleStepCommand(stateManager, teamManager, puzzleManager, worldManager));
    registerCommand(new PuzzleStartCommand(stateManager));
    registerCommand(new PuzzleResetCommand(stateManager));
  }

  // FIXME puzzle test POC
  private void setupSimplePuzzle() {
    resetSetup();
    teamManager.createTeam("test", "test", ChatColor.DARK_PURPLE);

    puzzleManager.modifyRoadmap(
      roadmap -> {
        roadmap.addRoom(
          roomManager.createRoom("test1", NamespacedKey.fromString("creative_redstone:puzzle_1"), room -> room.getSteps().add("test")));
        roadmap.addRoom(
          roomManager.createRoom("test2", NamespacedKey.fromString("creative_redstone:puzzle_2"), room -> room.getSteps().add("test")));
        roadmap.addRoom(
          roomManager.createRoom("test3", NamespacedKey.fromString("creative_redstone:puzzle_3"), room -> room.getSteps().add("test")));
        roadmap.addRoom(
          roomManager.createRoom("test4", NamespacedKey.fromString("creative_redstone:puzzle_4"), room -> room.getSteps().add("test")));
        roadmap.addRoom(
          roomManager.createRoom("test5", NamespacedKey.fromString("creative_redstone:puzzle_5"), room -> room.getSteps().add("test")));
        roadmap.addRoom(
          roomManager.createRoom("test6", NamespacedKey.fromString("creative_redstone:puzzle_6"), room -> room.getSteps().add("test")));
      });

    Bukkit.getPluginManager().registerEvents(new Listener() {
      @EventHandler
      void onJoin(PlayerJoinEvent event) {
        teamManager.addMember("test", event.getPlayer().getUniqueId());
      }
    }, this);
    Bukkit.getOnlinePlayers().forEach(player -> teamManager.addMember("test", player.getUniqueId()));
  }

  // FIXME puzzle test POC
  private void resetSetup() {
    puzzleManager.clearRoadmap();
    roomManager.clearRooms();
    teamManager.clearTeams();
  }

  @Override
  public void onDisable() {
    stateManager.setState(new DisablingState());
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
      .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
      .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
      .findAndRegisterModules()
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
