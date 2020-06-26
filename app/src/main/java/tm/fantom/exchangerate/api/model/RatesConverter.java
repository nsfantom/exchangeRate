package tm.fantom.exchangerate.api.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import tm.fantom.exchangerate.util.DateUtils;

public class RatesConverter implements JsonDeserializer<RateValue> {

    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    public RatesConverter() {
        this.dateTimeFormatter = DateUtils.dateFmt;
    }

    @Override
    public RateValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> elementEntry:jsonObject.entrySet()) {
            return new RateValue(elementEntry.getValue().getAsFloat());
        }
        return new RateValue(0);
    }
}