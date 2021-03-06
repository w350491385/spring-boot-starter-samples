package com.github.vindell.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.github.vindell.websocket.handler.BroadcastWebSocketsHandler;
import com.github.vindell.websocket.handler.MessageEventWebSocketHandler;
import com.github.vindell.websocket.interceptor.HandshakeSessionInterceptor;
import com.github.vindell.websocket.session.SessionFilter;
import com.github.vindell.websocket.session.SessionUriPathFilter;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	@Qualifier("dashboardWebSocketsHandler")
	private BroadcastWebSocketsHandler dashboardWebSocketsHandler;

	@Bean("dashboardSessionFilter")
	public SessionFilter dashboardSessionFilter() {
		return new SessionUriPathFilter("/metric-dashboard/**");
	}

	@Bean("dashboardWebSocketsHandler")
	public BroadcastWebSocketsHandler dashboardWebSocketsHandler(
			@Qualifier("dashboardSessionFilter") SessionFilter filter) {
		return new BroadcastWebSocketsHandler(filter);
	}

	@Autowired
	private MessageEventWebSocketHandler messageEventWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(messageEventWebSocketHandler, "/metrics")
				.addInterceptors(new HandshakeSessionInterceptor()).setAllowedOrigins("*").withSockJS();

	}

}
