/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    11:25:43 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.json;

import java.math.BigDecimal;

import tools.jackson.core.json.JsonWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.datatype.jsr310.JavaTimeFeature;
import tools.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 
 */
public class JsonMapperBuilder {

    // thread-safe
    private static final ObjectMapper DEFAULT_MAPPER = build();

    private static ObjectMapper build() {

        return JsonMapper.builder()
                .addModule(new JavaTimeModule()
                        .enable(JavaTimeFeature.ALWAYS_ALLOW_STRINGIFIED_DATE_TIMESTAMPS))
                .addModule(new SimpleModule("FitlyJsonConverters")
                        .addSerializer(BigDecimal.class, new BigDecimalSerializer())
                        .addDeserializer(BigDecimal.class, new BigDecimalDeserializer()))
                .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public static ObjectMapper get() {
        return DEFAULT_MAPPER;
    }

}
