package fr.syl2010.minecraft.CreativeRedstonePuzzle.team.serialization;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class GameTeamIdSerializer extends StdSerializer<GameTeam> {

  private static final long serialVersionUID = 4309623742158546425L;

  public GameTeamIdSerializer() {
    super(GameTeam.class);
  }

  @Override
  public void serialize(GameTeam value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(value.getId());
  }

}
