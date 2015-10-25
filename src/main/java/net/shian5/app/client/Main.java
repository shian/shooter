package net.shian5.app.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

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
    Button sendButton;

    private Timer animTimer = null;
    private long startTime;
    private long targetTime;

    private enum stage {IDLE, START, CHECK}

    ;
    private stage currentStage = stage.IDLE;

    //private ChannelServiceAsync chatRpc;

    public Main() {
        initWidget(ourUiBinder.createAndBindUi(this));
        timeField.setText("--");
        idle();
    }

    private void updateTimer() {
        if (currentStage == stage.START) {
            long currentTime = System.currentTimeMillis();
            if (startTime==0){
                startTime = currentTime;
                targetTime = startTime + 1000 * 10;
            }

            long diff = targetTime - currentTime;
            if (diff > 5000) {
                NumberFormat n = NumberFormat.getFormat("#,##0.000");
                timeField.setText(n.format(diff / 1000.0));
            }
//            else {
//                NumberFormat n = NumberFormat.getFormat("#,##0.000");
//                timeField.setText(n.format(diff / 1000.0));
//            }
        }
    }

    private void idle() {
        startTime = 0;
        targetTime = 0;
        currentStage = stage.IDLE;
        sendButton.setEnabled(true);
        sendButton.setText("Start");
    }

    private void start() {
        sendButton.setText("Shoot");
        currentStage = stage.START;

        if (animTimer != null) {
            animTimer.cancel();
            animTimer = null;
        }
        animTimer = new Timer() {
            @Override
            public void run() {
                updateTimer();
            }
        };
        animTimer.scheduleRepeating(100);
    }

    private void check() {
        if (animTimer != null) {
            animTimer.cancel();
            animTimer = null;
        }
        sendButton.setEnabled(false);
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - targetTime;
        timeField.setText(String.valueOf(diff));
        sendTickToServer((int) diff);
        currentStage = stage.CHECK;
    }

    @UiHandler("sendButton")
    public void handleClick(ClickEvent event) {
        if (currentStage == stage.IDLE) {
            start();
        } else if (currentStage == stage.START) {
            check();
        }
    }

    private void sendTickToServer(int tick) {
        greetingService.greetServer(tick, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                // Show the RPC error message to the user
                //dialogBox.setText("Remote Procedure Call - Failure");
                //serverResponseLabel.addStyleName("serverResponseLabelError");
                //serverResponseLabel.setHTML(SERVER_ERROR);
                //dialogBox.center();
                //closeButton.setFocus(true);
                Window.alert("server error");
                idle();
            }

            public void onSuccess(String result) {
                //dialogBox.setText("Remote Procedure Call");
                //serverResponseLabel.removeStyleName("serverResponseLabelError");
                //serverResponseLabel.setHTML(result);
                //dialogBox.center();
                //closeButton.setFocus(true);
                //Window.alert(result);
                idle();
            }
        });
    }
}