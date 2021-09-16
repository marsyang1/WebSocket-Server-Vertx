package com.vertx.websocket.server.handler;

import com.vertx.websocket.server.common.Constants;
import com.vertx.websocket.server.common.Producer;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Created by jiancai.wang on 2017/4/19. */
@RequiredArgsConstructor
public class AccessHandler {

  private final Vertx vertx;

  public void handle(RoutingContext routingContext) {
    // get topic form request
    String topicInfo = routingContext.request().getParam(Constants.DEFAULT_TOPIC_KEY);
    String[] topicArray =
        Objects.isNull(topicInfo) ? Constants.DEFAULT_TOPIC_LIST : topicInfo.trim().split(",");

    Producer producer =
        new Producer(
            vertx,
            routingContext.request().toWebSocket().result(),
            Stream.of(topicArray).collect(Collectors.toList()));
    producer.onOpen().access();
  }
}
