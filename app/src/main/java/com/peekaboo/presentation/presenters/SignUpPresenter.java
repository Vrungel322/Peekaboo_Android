package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.peekaboo.data.Constants;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.SignUpUseCase;
import com.peekaboo.presentation.utils.CredentialUtils;
import com.peekaboo.presentation.views.ICredentialsView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class SignUpPresenter extends ProgressPresenter<ICredentialsView> implements ISignUpPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private SignUpUseCase useCase;
    private WebSocket ws;
    private String TAG = "socket";

    @Inject
    public SignUpPresenter(Context context, SignUpUseCase useCase, ErrorHandler errorHandler) {
        super(context, errorHandler);
        this.useCase = useCase;
    }

    @Override
    public void onSignUpButtonClick(String login, String email, String password, String passwordConfirm) {
        if (isValid(login, email, password, passwordConfirm)) {
            useCase.setCredentials(login, password, email);
            useCase.execute(new BaseProgressSubscriber<User>(this) {
                @Override
                public void onNext(User response) {
                    super.onNext(response);
                    Log.e("onNext", String.valueOf(response));
//                if (getView() != null) {
//                    getView().navigateToProfile();
//                }
                    start(response);
                }
            });
        }
    }

    private boolean isValid(String login, String email, String password, String passwordConfirm) {

        if (!CredentialUtils.isLoginValid(login)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.LOGIN);
        } else if (!CredentialUtils.isPasswordValid(password)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.PASSWORD);
        } else if (!CredentialUtils.isPasswordConfirmed(password, passwordConfirm)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.PASSWORD_CONFIRM);
        } else if (!CredentialUtils.isEmailValid(email)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.EMAIL);
        } else {
            return true;
        }
        return false;
    }


    private void start(User user) {

        final String wsuri = Constants.BASE_URL_SOCKET;

        Log.e(TAG, user.getBearer());
        try {
            ws = new WebSocketFactory().createSocket(wsuri)

                    .addListener(new WebSocketAdapter() {
                        @Override
                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            Log.e(TAG, "Status: Connected to " + wsuri);
                            ws.clearHeaders();
                            sendMessage();
                        }

                        @Override
                        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                            Log.e(TAG, "Status: Error " + cause);
                        }

                        @Override
                        public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                            Log.e(TAG, "Status: Connection Error " + cause);
                        }

                        @Override
                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            Log.e(TAG, "Status: Disconnected ");
                        }

                        @Override
                        public void onTextMessage(WebSocket websocket, String text) throws Exception {
                            Log.e(TAG, "Status: Text Message received" + text);
                        }
                    })
                    .addHeader("Authorization", user.getBearer())
                    .connectAsynchronously();
        } catch (IOException e) {
            Log.e(TAG, "exception " + e);
        }
    }


    public void sendMessage() {
        if (ws != null)
            ws.sendText("hi");
    }
}
