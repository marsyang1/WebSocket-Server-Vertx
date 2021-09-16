package com.vertx.websocket.main;

import com.vertx.websocket.server.verticles.AcceptorVerticle;
import com.vertx.websocket.server.verticles.HeartbeatVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import static com.vertx.websocket.server.util.PropertiesUtil.loadProperties;

/**
 * 參考文件 https://devcenter.heroku.com/articles/websocket-security#validate-client-input When the
 * client-side code decides to open a WebSocket, it contacts the HTTP server to obtain an
 * authorization “ticket”.
 *
 * <p>The server generates this ticket. It typically contains some sort of user/account ID, the IP
 * of the client requesting the ticket, a timestamp, and any other sort of internal record keeping
 * you might need.
 *
 * <p>The server stores this ticket (i.e. in a database or cache), and also returns it to the
 * client.
 *
 * <p>The client opens the WebSocket connection, and sends along this “ticket” as part of an initial
 * handshake.
 *
 * <p>The server can then compare this ticket, check source IPs, verify that the ticket hasn’t been
 * re-used and hasn’t expired, and do any other sort of permission checking. If all goes well, the
 * WebSocket connection is now verified.
 */
@Slf4j
public class SingleMainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // load config
    JsonObject conf = loadProperties(StartWebSocket.DEFAULT_CONFIG_RES);
    // deploy
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setConfig(conf);
    // acceptor request
    vertx.deployVerticle(AcceptorVerticle.class.getName(), deploymentOptions);
    // heartbeat
    vertx.deployVerticle(HeartbeatVerticle.class.getName(), deploymentOptions);
    log.info("SingleMainVerticle deployVerticle finished ........");
  }
}
