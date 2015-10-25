package net.shian5.app.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import dontCare.gf.gae.gwtChannel.client.Channel;
import dontCare.gf.gae.gwtChannel.client.event.CloseEvent;
import dontCare.gf.gae.gwtChannel.client.event.ErrorEvent;
import dontCare.gf.gae.gwtChannel.client.event.MessageEvent;
import dontCare.gf.gae.gwtChannel.client.event.OpenEvent;
import net.shian5.app.shared.ChatMessage;

/**
 * Created by shian on 2015/10/23.
 */
public class MsgPanel extends Composite {
    interface MsgPanelUiBinder extends UiBinder<HTMLPanel, MsgPanel> {
    }

    private static MsgPanelUiBinder ourUiBinder = GWT.create(MsgPanelUiBinder.class);
    private final ChannelServiceAsync channelService = GWT.create(ChannelService.class);

    @UiField
    UListElement msgs;

    private Channel chatChannel=null;

    public MsgPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));

        appendMessage("Initial.");
        channelService.getToken(new AsyncCallback<String>() {
            //skip

            @Override
            public void onSuccess(String token) {
                appendMessage("get token");

                chatChannel = new Channel(token);
                chatChannel.addOpenHandler(new OpenEvent.OpenHandler() {
                    @Override
                    public void onOpen(OpenEvent openEvent) {
                        appendMessage("Channel open");
                    }
                });
                chatChannel.addMessageHandler(new MessageEvent.MessageHandler() {
                    @Override
                    public void onMessage(MessageEvent messageEvent) {
                        ChatMessage cm = (ChatMessage) messageEvent.getMessage();
                        appendMessage(cm.getName() + ":" + cm.getMsg());
                    }
                });
                chatChannel.addErrorHandler(new ErrorEvent.ErrorHandler() {
                    @Override
                    public void onError(ErrorEvent errorEvent) {
                        appendMessage("channel error");
                    }
                });
                chatChannel.addCloseHandler(new CloseEvent.CloseHandler() {
                    @Override
                    public void onClose(CloseEvent closeEvent) {
                        appendMessage("channel close");
                    }
                });
                chatChannel.open();
                appendMessage("system up");
            }

            @Override
            public void onFailure(Throwable caught) {
                appendMessage(caught.getMessage());
            }
        });
    }

    private void appendMessage(String msg) {
        LIElement element = Document.get().createLIElement();
        element.setInnerText(msg);
        msgs.appendChild(element);
    }
}