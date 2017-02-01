package com.github.fabienrenaud.jjb.provider;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.owlike.genson.Genson;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.apache.johnzon.mapper.Mapper;

import java.util.Map;

/**
 * Created by frenaud on 7/24/16.
 */
public interface JsonProvider<T> {

    Gson gson();

    ObjectMapper jackson();

    ObjectMapper jacksonAfterburner();

    JsonFactory jacksonFactory();

    Genson genson();

    JSONDeserializer<T> flexjsonDeser();

    JSONSerializer flexjsonSer();

    org.boon.json.ObjectMapper boon();

    Mapper johnson();

    Map<String, Object> jsonioStreamOptions();

    DslJson<T> dsljson();

    jodd.json.JsonParser joddDeser();

    jodd.json.JsonSerializer joddSer();

    com.squareup.moshi.JsonAdapter<T> moshi();

    JsonIterator jsonIterator();

    JsonStream jsonStream();
}
