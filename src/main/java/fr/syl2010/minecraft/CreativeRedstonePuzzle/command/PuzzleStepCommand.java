package fr.syl2010.minecraft.CreativeRedstonePuzzle.command;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.Utils;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.WorldManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.PuzzleManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.PuzzleRoomInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.PuzzleRoomInstance.StepResult;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.RoadmapInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.StateManager;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.EndingState;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states.PlayingState;

public class PuzzleStepCommand {

  private final StateManager  stateManager;
  private final PuzzleManager puzzleManager;
  private final WorldManager  worldManager;

  public PuzzleStepCommand(StateManager stateManager, PuzzleManager puzzleManager, WorldManager worldManager) {
    this.stateManager = stateManager;
    this.puzzleManager = puzzleManager;
    this.worldManager = worldManager;
  }

  @Suggestions("step")
  public List<String> stepSuggestion(CommandContext<CommandSender> sender, String input) {
    World world;
    if (sender instanceof BlockCommandSender blockSender) {
      world = blockSender.getBlock().getWorld();
    } else if (sender instanceof Entity entity) {
      world = entity.getWorld();
    } else return List.of();
    RoadmapInstance roadmap = puzzleManager.getRunningRoadmap(world);
    if (roadmap == null) return List.of();
    else return roadmap.getCurrentRoom().getSteps().stream().filter(step -> step.startsWith(input)).toList();
  }

  @CommandMethod("puzzle trigger <step>")
  public void triggerStep(CommandSender sender, @Argument("step") String step) {
    if (!stateManager.isCurrentState(PlayingState.class)) {
      sender.sendMessage("§cYou can't trigger a step when no game is running");
      return;
    }

    World world;
    if (sender instanceof BlockCommandSender blockSender) {
      world = blockSender.getBlock().getWorld();
    } else if (sender instanceof Entity entity) {
      world = entity.getWorld();
    } else {
      sender.sendMessage("You can't complete a step from a console");
      return;
    }

    RoadmapInstance roadmap = puzzleManager.getRunningRoadmap(world);
    if (roadmap == null) {
      sender.sendMessage("§cNo running game found in this world");
    } else {
      PuzzleRoomInstance room = roadmap.getCurrentRoom();
      StepResult result;

      if (sender instanceof BlockCommandSender blockSender) {
        Block commandBlock = blockSender.getBlock();

        result = room.triggerStep(commandBlock, step);
      } else {
        result = room.completeStep(step);
      }

      String message;

      switch (result) {
        case ALREADY_COMPLETED_ROOM:
          message = "§eRoom already completed";
          break;
        case ALREADY_COMPLETED_STEP:
          message = "§eStep already completed";
          break;
        case NOT_POWERED_STEP:
          message = "§cCommand block not powered";
          break;
        case UNKNOWN_STEP:
          message = "§cUnknown step for this room";
          break;
        case COMPLETED_STEP:
          message = "§aStep completed";
          break;
        case COMPLETED_ROOM:
          try {
            if (!Utils.syncAndAwait(() -> roadmap.teleportToNextRoom(), CreativeRedstonePuzzlePlugin.getPlugin())) {
              sender.sendMessage("§a§lMap completed!");

              Bukkit.getScheduler().runTask(CreativeRedstonePuzzlePlugin.getPlugin(), () -> {
                // TODO proper team win
                for (UUID member : roadmap.getTeam().getMembers()) {
                  Player player = Bukkit.getPlayer(member);
                  if (player != null) {
                    world.getNearbyEntities(player.getLocation(), 3, 3, 3, intEntity -> intEntity.getType() == EntityType.DROPPED_ITEM)
                      .forEach(Entity::remove);
                    player.getInventory().clear();
                    player.teleport(worldManager.getLobbyWorld().getSpawnLocation());
                  }
                }
                Bukkit.broadcastMessage(String.format("§2Team %s has completed all their puzzles", roadmap.getTeam().getDisplayName()));

                // detect if all map finished
                if (puzzleManager.isEnded()) {
                  stateManager.setState(new EndingState());
                }
              });
              return;
            } else {
              message = "§aRoom completed";
            }
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          break;
        default:
          throw new NoSuchElementException(String.format("Unknown result %s", result));
      }

      sender.sendMessage(message);
    }

  }

}
