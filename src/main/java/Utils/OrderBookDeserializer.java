package Utils;

import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;

public interface OrderBookDeserializer<T> extends JsonDeserializer<T> {

    Type getType();
}
