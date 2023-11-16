package com.jxm.chat.tio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 开启类
 */
@Component
public class StartTioRunner implements CommandLineRunner {

    @Resource
    private WsMsgHandler tioWsMsgHandler;

    private WebsocketStarter appStarter;

    @Override
    public void run(String... args) throws Exception {
        appStarter = new WebsocketStarter(ServerConfig.SERVER_PORT, tioWsMsgHandler);
        appStarter.getWsServerStarter().start();
    }

    public WebsocketStarter getAppStarter() {
        return appStarter;
    }
}
