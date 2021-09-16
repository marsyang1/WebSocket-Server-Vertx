package com.vertx.websocket.server.util;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigRetrieverUtil {

  public static ConfigRetriever getConfigRetrieverFromFile(Vertx vertx, String filePath) {
    // Create the config retriever
    ConfigRetriever retriever =
        ConfigRetriever.create(
            vertx,
            new ConfigRetrieverOptions()
                .addStore(
                    new ConfigStoreOptions()
                        .setType("file")
                        .setConfig(new JsonObject().put("path", filePath))));
    return retriever;
  }

}
