package org.noxnox.hackathontennis;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
   private static final DateTimeFormatter FORMATTER;

   static {
      FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
   }

   public OffsetDateTimeAdapter() {
   }

   public JsonElement serialize(OffsetDateTime src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(FORMATTER.format(src));
   }

   public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return (OffsetDateTime)FORMATTER.parse(json.getAsString(), OffsetDateTime::from);
   }
}

