package com.vertx.websocket.server.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.vertx.websocket.server.util.PropertiesUtil.loadProperties;

/** Created by jiancai.wang on 2017/5/10. */
@Slf4j
public class StartWebSocketVerticle extends AbstractVerticle {

  private static final String DEFAULT_CONFIG_RES = "configs/default-websocket.json";

  @Override
  public void start() throws Exception {

    // load config
    JsonObject config = Objects.isNull(config()) ? config() : loadProperties(DEFAULT_CONFIG_RES);
    Vertx vertx = getVertx();
    log.info(
        "vertx initialized successfully, isClustered: {} detail: {} ", vertx.isClustered(), vertx);
    // deploy
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setConfig(config);
    // acceptor request
    vertx.deployVerticle(AcceptorVerticle.class.getName(), deploymentOptions);
    // heartbeat
    vertx.deployVerticle(HeartbeatVerticle.class.getName(), deploymentOptions);
  }
}
