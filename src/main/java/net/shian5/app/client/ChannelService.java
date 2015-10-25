package net.shian5.app.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 建立 Channel 並提供 token
 *
 */
@RemoteServiceRelativePath("channel")
public interface ChannelService extends RemoteService {
    String getToken();
}
