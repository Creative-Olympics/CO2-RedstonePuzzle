package fr.syl2010.minecraft.CreativeRedstonePuzzle.team.serialization;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class GameTeamIdDeserializer extends StdDeserializer<GameTeam> {

  private static final long serialVersionUID = 4309623742158546425L;

  public GameTeamIdDeserializer() {
    super(GameTeam.class);
  }

  @Override
  public GameTeam deserialize(JsonParser json, DeserializationContext ctxt) throws IOException {
    return CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager().getTeam(json.readValueAs(String.class));
  }

}
