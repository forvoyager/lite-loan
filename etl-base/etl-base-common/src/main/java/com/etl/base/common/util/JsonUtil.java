package com.etl.base.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json operation.
 */
public class JsonUtil {

  private static class ObjectMapperHolder {
    private static ObjectMapper JACKSON_OBJECT_MAPPER = new ObjectMapper(); // thread-safe

    static {
//      JACKSON_OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true); // format json
      JACKSON_OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);

      // 忽略目标对象没有的属性
      JACKSON_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getMapper() {
      return JACKSON_OBJECT_MAPPER;
    }
  }

  /**
   * convert object to json string
   *
   * @param obj
   * @return
   */
  public static String parseJson(Object obj) {
    try {
      return ObjectMapperHolder.getMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  /**
   * parse json string to object(type clazz)
   *
   * @param json  string
   * @param clazz target type
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T extends Object> T parseObject(String json, Class<T> clazz) throws IOException {
    return ObjectMapperHolder.getMapper().readValue(json, clazz);
  }

  /**
   * parse json string to JsonNode(tree)
   *
   * @param json string
   * @return
   * @throws IOException
   */
  public static JsonNode parseObject(String json) throws IOException {
    return ObjectMapperHolder.getMapper().reader().readTree(json);
  }

  /**
   * parse json string to object
   *
   * @param json
   * @param typeReference eg: new TypeReference<Map<String, Object>>(){}
   * @return
   * @throws IOException
   */
  public static Map parseObject(String json, TypeReference typeReference) throws IOException {
    return ObjectMapperHolder.getMapper().readValue(json, typeReference);
  }

  /**
   * parse json string to Map<String, clazz>
   *
   * @param json
   * @param clazz
   * @return
   * @throws IOException
   */
  public static <T> Map<String, T> parseMap(String json, Class<T> clazz) throws IOException {
    JavaType javaType = ObjectMapperHolder.getMapper().getTypeFactory().constructParametricType(HashMap.class, String.class, clazz);
    return ObjectMapperHolder.getMapper().readValue(json, javaType);
  }

  /**
   * parse json string to List<clazz>
   *
   * @param json
   * @param clazz
   * @return
   * @throws IOException
   */
  public static <T> List<T> parseList(String json, Class<T> clazz) throws IOException {
    JavaType javaType = ObjectMapperHolder.getMapper().getTypeFactory().constructParametricType(List.class, clazz);
    return ObjectMapperHolder.getMapper().readValue(json, javaType);
  }
}
