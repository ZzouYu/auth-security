package com.tuling.config.indb;

import com.alibaba.fastjson.util.IOUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * json序列化器
 */
@Component
public class JsonSerializationStrategy extends StandardStringSerializationStrategy {


    private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer= new GenericJackson2JsonRedisSerializer();
    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(bytes,clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return jackson2JsonRedisSerializer.deserialize(bytes,clazz);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return jackson2JsonRedisSerializer.serialize(object);
    }
}
