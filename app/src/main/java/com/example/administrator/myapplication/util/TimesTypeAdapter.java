package com.example.administrator.myapplication.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by king on 2016/10/15.
 */
public class TimesTypeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {
    private final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }

        try {
            Date date = (Date) format.parse(json.getAsString());
            return new Time(date.getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }

    }

    @Override
    public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
        String dfString = format.format(new Date(src.getTime()));
        return new JsonPrimitive(dfString);
    }
}

