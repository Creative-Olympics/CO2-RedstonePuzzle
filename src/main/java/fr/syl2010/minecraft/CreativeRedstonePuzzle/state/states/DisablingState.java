package fr.syl2010.minecraft.CreativeRedstonePuzzle.state.states;

import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.state.State;

public class DisablingState implements State {

  @Override
  public void onEnter() {
    CreativeRedstonePuzzlePlugin.getPlugin().getPuzzleManager().deleteWorlds().join();
  }

  @Override
  public void onExit() {}

}
