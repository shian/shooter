package net.shian5.app.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import dontCare.gf.gae.gwtChannel.client.Channel;
import dontCare.gf.gae.gwtChannel.client.ChannelServiceAsync;
import dontCare.gf.gae.gwtChannel.client.event.CloseEvent;
import dontCare.gf.gae.gwtChannel.client.event.ErrorEvent;
import dontCare.gf.gae.gwtChannel.client.event.MessageEvent;
import dontCare.gf.gae.gwtChannel.client.event.OpenEvent;
import net.shian5.app.shared.ChatMessage;
import net.shian5.app.shared.FieldVerifier;

import java.util.Date;

/**
 * Created by shian on 2015/10/23.
 */
public class Main extends Composite {
    interface mainUiBinder extends UiBinder<HTMLPanel, Main> {
    }

    private static mainUiBinder ourUiBinder = GWT.create(mainUiBinder.class);

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    @UiField
    Label timeField;
    @UiField
    Label errorLabel;
    @UiField
    TextBox nameField;
    @UiField
    Button sendButton;

    private Timer animTimer = null;
    private long startTime;
    private long targetTime;
    private enum stage { IDLE, START, CHECK };
    private stage currentStage = stage.IDLE;

    private ChannelServiceAsync chatRpc;

    public Main() {
        initWidget(ourUiBinder.createAndBindUi(this));

        updateTimer();
        if (animTimer == null) {
            animTimer = new Timer() {
                @Override
                public void run() {
                    updateTimer();
                }
            };
        }
        animTimer.scheduleRepeating(1000);

        sendButton.setText("Start");

        chatRpc = Channel.getService("chat");
    }

    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        if (currentStage==stage.START) {
            long diff = targetTime - currentTime;
            if (diff > 3000) {
                NumberFormat n = NumberFormat.getFormat("#,##0.000");
                timeField.setText(n.format(currentTime / 1000.0));
            }
        } else {
            NumberFormat n = NumberFormat.getFormat("#,##0.000");
            timeField.setText(n.format(currentTime / 1000.0));
        }
    }

    @UiHandler("sendButton")
    public void handleClick(ClickEvent event) {
        if(currentStage==stage.IDLE) {
            nameField.setText("");
            startTime = System.currentTimeMillis();
            targetTime = startTime + 1000*5;
            sendButton.setText("Shoot");
            currentStage = stage.START;
        }else if(currentStage==stage.START){
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - targetTime;
            nameField.setText(String.valueOf(diff));
            sendNameToServer();
/*
            chatRpc.sendMessage("channel_demo", new ChatMessage("test1", String.valueOf(diff)),
                    new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error: "+caught);
                            currentStage = stage.IDLE;
                            sendButton.setText("Start");
                        }

                        @Override
                        public void onSuccess(Void result) {
                            currentStage = stage.IDLE;
                            sendButton.setText("Start");
                        }
                    });
*/

            currentStage = stage.CHECK;
        }
    }

    //
    // Send the name from the nameField to the server and wait for a response.
    //
    private void sendNameToServer() {
        // First, we validate the input.
        errorLabel.setText("");
        String textToServer = nameField.getText();
        if (!FieldVerifier.isValidName(textToServer)) {
            errorLabel.setText("Please enter at least four characters");
            currentStage = stage.IDLE;
            return;
        }

        // Then, we send the input to the server.
        sendButton.setEnabled(false);
        //textToServerLabel.setText(textToServer);
        //serverResponseLabel.setText("");
        greetingService.greetServer(textToServer, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                // Show the RPC error message to the user
                //dialogBox.setText("Remote Procedure Call - Failure");
                //serverResponseLabel.addStyleName("serverResponseLabelError");
                //serverResponseLabel.setHTML(SERVER_ERROR);
                //dialogBox.center();
                //closeButton.setFocus(true);
                Window.alert("server error");
                currentStage = stage.IDLE;
                sendButton.setEnabled(true);
                sendButton.setText("Start");
            }

            public void onSuccess(String result) {
                //dialogBox.setText("Remote Procedure Call");
                //serverResponseLabel.removeStyleName("serverResponseLabelError");
                //serverResponseLabel.setHTML(result);
                //dialogBox.center();
                //closeButton.setFocus(true);
                //Window.alert(result);
                currentStage = stage.IDLE;
                sendButton.setEnabled(true);
                sendButton.setText("Start");
            }
        });
    }
}