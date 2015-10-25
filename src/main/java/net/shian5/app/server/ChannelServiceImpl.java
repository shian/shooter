package net.shian5.app.server;

import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.shian5.app.client.ChannelService;

/**
 * 建立 Channel 並提供 token
 */
public class ChannelServiceImpl extends RemoteServiceServlet implements ChannelService {

    private static String token = null;

    @Override
    public String getToken() {
        if (token==null) {
            token = ChannelServiceFactory.getChannelService().createChannel("shooter");
        }
        return token;
    }
}
