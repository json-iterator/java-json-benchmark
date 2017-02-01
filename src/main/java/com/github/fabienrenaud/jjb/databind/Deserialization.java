package com.github.fabienrenaud.jjb.databind;

import com.alibaba.fastjson.JSON;
import com.bluelinelabs.logansquare.LoganSquare;
import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabienrenaud.jjb.Cli;
import com.github.fabienrenaud.jjb.Config;
import com.github.fabienrenaud.jjb.JsonBench;
import com.google.gson.JsonSyntaxException;
import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.TypeLiteral;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;


/**
 * @author Fabien Renaud
 */
@State(Scope.Thread)
public class Deserialization extends JsonBench {

    private ObjectMapper jackson;
    private byte[] nextByteArray;
    private Class pojoType;
    private JsonIterator jsonIterator;
    private TypeLiteral pojoTypeLiteral;
    private DslJson dsljson;

    @Setup(Level.Trial)
    public void benchSetup(BenchmarkParams params) {
        Cli.AbstractCommand cmd = Config.load();
        cmd.sizeOfEachPayloadInKb = 10;
        Config.save(cmd);
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        jackson = JSON_SOURCE.provider().jackson();
        nextByteArray = JSON_SOURCE.nextByteArray();
        pojoType = JSON_SOURCE.pojoType();
        jsonIterator = new JsonIterator();
        pojoTypeLiteral = JSON_SOURCE.pojoTypeLiteral();
        dsljson = JSON_SOURCE.provider().dsljson();
    }

    @Benchmark
    @Override
    public Object gson() throws Exception {
        return JSON_SOURCE.provider().gson().fromJson(JSON_SOURCE.nextReader(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object jackson() throws Exception {
        return JSON_SOURCE.provider().jackson().readValue(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    public void jackson_(Blackhole bh) throws Exception {
        bh.consume(jackson.readValue(nextByteArray, pojoType));
    }

    @Benchmark
    @Override
    public Object jackson_afterburner() throws IOException {
        return JSON_SOURCE.provider().jacksonAfterburner().readValue(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object genson() throws JsonSyntaxException {
        return JSON_SOURCE.provider().genson().deserialize(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object fastjson() {
        return JSON.parseObject(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object flexjson() throws JsonSyntaxException {
        return JSON_SOURCE.provider().flexjsonDeser().deserialize(JSON_SOURCE.nextReader(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object boon() throws Exception {
        return JSON_SOURCE.provider().boon().readValue(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object johnzon() throws Exception {
        return JSON_SOURCE.provider().johnson().readObject(JSON_SOURCE.nextInputStream(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object jsonsmart() throws Exception {
        return net.minidev.json.JSONValue.parse(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object dsljson() throws Exception {
        byte[] buffer = JSON_SOURCE.nextByteArray();
        return JSON_SOURCE.provider().dsljson().deserialize(JSON_SOURCE.pojoType(), buffer, buffer.length);
    }

    @Benchmark
    public void dsljson_(Blackhole bh) throws Exception {
        bh.consume(dsljson.deserialize(pojoType, nextByteArray, nextByteArray.length));
    }

    @Benchmark
    @Override
    public Object logansquare() throws Exception {
        return LoganSquare.parse(JSON_SOURCE.nextInputStream(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object jodd() throws Exception {
        return JSON_SOURCE.provider().joddDeser().parse(JSON_SOURCE.nextString(), JSON_SOURCE.pojoType());
    }

    @Benchmark
    @Override
    public Object moshi() throws Exception {
        return JSON_SOURCE.provider().moshi().fromJson(JSON_SOURCE.nextOkioBufferedSource());
    }

    @Benchmark
    @Override
    public Object jsoniter() throws Exception {
        return JsonIterator.deserialize(JSON_SOURCE.nextByteArray(), JSON_SOURCE.pojoTypeLiteral());
    }

    @Benchmark
    public void jsoniter_(Blackhole bh) throws Exception {
        jsonIterator.reset(nextByteArray);
        bh.consume(jsonIterator.read(pojoTypeLiteral));
    }

    public static void main(String[] args) throws IOException, RunnerException {
        Main.main(new String[]{
//                "databind.Deserialization.jsoniter_",
//                "databind.Deserialization.dsljson_",
                "databind.Deserialization.jackson_",
                "-i", "5",
                "-wi", "5",
                "-f", "1",
        });
    }
}
