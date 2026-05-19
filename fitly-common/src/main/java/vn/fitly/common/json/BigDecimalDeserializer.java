/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    11:18:14 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.json;

import java.math.BigDecimal;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.InvalidFormatException;

/**
 * 
 */
public class BigDecimalDeserializer extends StdDeserializer<BigDecimal> {

    protected BigDecimalDeserializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {

        try {
            if (p.currentToken() == JsonToken.VALUE_NUMBER_FLOAT
                    || p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                return p.getDecimalValue();
            }

            String text = p.getString();
            if (text.isEmpty()) {
                return null;
            }

            return new BigDecimal(text);

        } catch (NumberFormatException e) {
            throw InvalidFormatException.from(
                    p,
                    "Cannot deserialize value as BigDecimal: \"" + e.getMessage() + "\"",
                    p.currentToken(),
                    BigDecimal.class);
        }
    }

}
