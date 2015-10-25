package net.shian5.app.server;

import com.google.appengine.api.channel.ChannelServiceFactory;
import dontCare.gf.gae.gwtChannel.server.ChannelServiceServlet;
import net.shian5.app.shared.ChatMessage;

import java.util.HashMap;

/**
 * Created by shian on 2015/10/23.
 */
@SuppressWarnings("serial")
public class ChatServiceImpl extends ChannelServiceServlet<ChatMessage> {
    private static HashMap<String, String> tokenMap = new HashMap<String, String>();

    @Override
    public String getToken(String name) {
        if (tokenMap.get(name) == null) {
            tokenMap.put(
                    name,
                    ChannelServiceFactory.getChannelService().createChannel(name)
            );
        }
        return tokenMap.get(name);
    }



}
