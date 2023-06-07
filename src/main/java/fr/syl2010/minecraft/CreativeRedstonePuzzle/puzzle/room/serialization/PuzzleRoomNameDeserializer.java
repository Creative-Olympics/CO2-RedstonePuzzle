package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.serialization;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;

public class PuzzleRoomNameDeserializer extends StdDeserializer<PuzzleRoom> {

  private static final long serialVersionUID = 4309623742158546425L;

  public PuzzleRoomNameDeserializer() {
    super(PuzzleRoom.class);
  }

  @Override
  public PuzzleRoom deserialize(JsonParser json, DeserializationContext ctxt) throws IOException {
    return CreativeRedstonePuzzlePlugin.getPlugin().getRoomManager().getRoom(json.readValueAs(String.class));
  }

}
