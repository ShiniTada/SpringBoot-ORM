package com.epam.esm.dto.jsonview;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class DateDeserializer extends StdDeserializer<LocalDate> {

  protected DateDeserializer() {
    super(LocalDate.class);
  }

  @Override
  public LocalDate deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    return LocalDate.parse(parser.readValueAs(String.class));
  }
}
