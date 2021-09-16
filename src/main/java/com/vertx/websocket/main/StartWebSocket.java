package com.vertx.websocket.main;

import static com.vertx.websocket.server.util.PropertiesUtil.loadProperties;

import com.vertx.websocket.server.verticles.AcceptorVerticle;
import com.vertx.websocket.server.verticles.HeartbeatVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;

/** Created by jiancai.wang on 2017/4/19. */
@Slf4j
public class StartWebSocket {

  public static final String DEFAULT_CONFIG_RES = "configs/default-websocket.json";

  public static void main(String[] args) {

    // load config
    JsonObject conf = loadProperties(DEFAULT_CONFIG_RES);
    String mode = conf.getString("mode");
    if ("cluster".equals(mode)) {
      ClusterManager clusterManager = new ZookeeperClusterManager();
      VertxOptions options =
          new VertxOptions()
              .setClusterManager(clusterManager)
              .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
      Vertx.clusteredVertx(
          options,
          res -> {
            if (res.succeeded()) {
              // 初始化vertx
              Vertx vertx = res.result();
              log.info(
                  "vertx initialized successfully, isClustered: {} detail: {} ",
                  vertx.isClustered(),
                  vertx);
              // deploy
              DeploymentOptions deploymentOptions = new DeploymentOptions();
              deploymentOptions.setConfig(conf);
              // acceptor request
              vertx.deployVerticle(AcceptorVerticle.class.getName(), deploymentOptions);
              // heartbeat
              vertx.deployVerticle(HeartbeatVerticle.class.getName(), deploymentOptions);
            } else {
              log.error("Failed to init vertx, {}", res.cause().getMessage());
            }
          });
    } else {
      log.info("Starting Vertx by Single Instance Server .......................");
      Launcher.executeCommand("run", SingleMainVerticle.class.getName());
    }
  }
}
