package net.shian5.app.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface ChannelServiceAsync {
    void getToken(AsyncCallback<String> async);
}
