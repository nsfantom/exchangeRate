package tm.fantom.exchangerate.api.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class RatesConverter implements JsonDeserializer<RateValue> {

    @Override
    public RateValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
            return new RateValue(elementEntry.getValue().getAsFloat());
        }
        return new RateValue(0);
    }
}