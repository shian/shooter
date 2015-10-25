package net.shian5.app.server;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import dontCare.gf.gae.gwtChannel.client.Message;
import net.shian5.app.client.GreetingService;
import net.shian5.app.shared.ChatMessage;
import net.shian5.app.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.lang.reflect.Method;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
        GreetingService {

    public String greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(input)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
        }

        //String serverInfo = getServletContext().getServerInfo();
        //String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        //userAgent = escapeHtml(userAgent);

        try {
            sendToChannel("channel_demo", new ChatMessage("demo", input));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        //return "Hello, " + input + "!<br><br>I am running " + serverInfo
        //        + ".<br><br>It looks like you are using:<br>" + userAgent;
        return "OK";
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }

    interface MessageService {
        Message getMessage(Message msg);
    }

    private void sendToChannel(String channelName, ChatMessage msg) throws NoSuchMethodException, SerializationException {
        Method serviceMethod = MessageService.class.getMethod("getMessage", Message.class);
        String serialized = RPC.encodeResponseForSuccess(serviceMethod, msg, new SerializationPolicy() {
            @Override
            public void validateSerialize(Class<?> clazz) {
            }

            @Override
            public void validateDeserialize(Class<?> clazz) {
            }

            @Override
            public boolean shouldSerializeFields(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldDeserializeFields(Class<?> clazz) {
                return false;
            }
        });
        ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(channelName, serialized));
    }
}
