package com.clbee.pagebuilder.util;

import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DocumentEncoderTest {
    private final Log logger = LogFactory.getLog(getClass());

    static JsonParser parser = new JsonParser();

    @Test
    public void testResourceFile() throws IOException {
        File resource = new ClassPathResource("store-sample.json").getFile();
        String jsonString = new String(Files.readAllBytes(resource.toPath()));
        // assert (json.matches("[a-z]+"));

        try {
            JSONObject json = new JSONObject(jsonString);
            parse(json);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void parse(JSONObject json) throws JSONException {

        JSONObject pages = json.getJSONObject("pages");
        String fileName = json.getString("fileName");

        logger.info(fileName);
        parsePages(pages);

    }

    public void parsePages(JSONObject pages) {
        // JSONArray ja = pages.getAsJsonArray();
    }
}
