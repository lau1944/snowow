package com.vau.snowow.engine.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.models.Configuration;
import com.vau.snowow.engine.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Special parser for parsing formed data into actual value
 * @author liuquan
 */
@Slf4j
public final class ValueParser {

    private static final String HEADERS = "headers";
    private static final String PARAMS = "params";
    private static final String CONFIGS = "configs";

    public Object parse(String data) {
        if (!StringUtil.isFormValue(data)) {
            return data;
        }
        String value = data.substring(2, data.length() - 1);
        String[] args = value.split("\\.");
        return getTargetValue(args);
    }

    private Object getTargetValue(String[] target) {
        StringBuilder builder = new StringBuilder();

        String head = target[0];
        if (HEADERS.equals(head) || PARAMS.equals(head)) {
            builder.append(head);
            for (int i = 1; i < target.length; ++i) {
                builder.append(".get(\"" + target[i] + "\")");
            }
        } else if (CONFIGS.equals(head)) {
            Configuration configuration = SnowContext.loadConfiguration();
            JsonObject elements = configuration.getInElements().getAsJsonObject();
            JsonElement subElement = elements;
            for (int i = 1; i < target.length; ++i) {
                if (subElement.isJsonObject()) {
                    subElement = subElement.getAsJsonObject().get(target[i]);
                }

                if (subElement.isJsonPrimitive()) {
                    break;
                }
            }
            return subElement;
        }
        return builder.toString();
    }

    public static ValueParser newParser() {
        return new ValueParser();
    }
}
