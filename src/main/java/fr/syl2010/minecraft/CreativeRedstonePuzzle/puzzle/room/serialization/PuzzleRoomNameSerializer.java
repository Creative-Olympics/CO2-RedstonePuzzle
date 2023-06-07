package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.serialization;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;

public class PuzzleRoomNameSerializer extends StdSerializer<PuzzleRoom> {

  private static final long serialVersionUID = 4309623742158546425L;

  public PuzzleRoomNameSerializer() {
    super(PuzzleRoom.class);
  }

  @Override
  public void serialize(PuzzleRoom value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(value.getName());
  }

}
