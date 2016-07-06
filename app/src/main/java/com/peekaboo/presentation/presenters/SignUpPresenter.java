package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.peekaboo.R;
import com.peekaboo.data.Constants;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.ConfirmUseCase;
import com.peekaboo.domain.usecase.SignUpUseCase;
import com.peekaboo.presentation.utils.CredentialUtils;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.presentation.views.ISingUpView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class SignUpPresenter extends ProgressPresenter<ISingUpView> implements ISignUpPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private SignUpUseCase signUpUseCase;
    private ConfirmUseCase confirmUseCase;
    private WebSocket ws;
    private String TAG = "socket";
    private User user;

    @Inject
    public SignUpPresenter(Context context,
                           SignUpUseCase signUpUseCase, ConfirmUseCase confirmUseCase,
                           ErrorHandler errorHandler) {
        super(context, errorHandler);
        this.signUpUseCase = signUpUseCase;
        this.confirmUseCase = confirmUseCase;
    }

    @Override
    public void onSignUpButtonClick(String login, String password, String passwordConfirm) {

        if (isValid(login, password, passwordConfirm)) {
            signUpUseCase.setCredentials(login, password);
            signUpUseCase.execute(new BaseProgressSubscriber<User>(this) {
                @Override
                public void onNext(User response) {
                    super.onNext(response);
                    Log.e("onNext", String.valueOf(response));
                    user = response;
                    if (getView() != null)
                        getView().showConfirmDialog();
//                if (getView() != null) {
//                    getView().navigateToProfile();
//                }
                    start(response);
                    Toast.makeText(getContext(), "onNext", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    getView().showConfirmDialog();
                    Toast.makeText(getContext(), "onComplete", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onCodeConfirmButtonClick(String key) {
        if (isValid(key)) {
            confirmUseCase.setConfirmData(user.getId(), key);
            confirmUseCase.execute(new BaseProgressSubscriber<User>(this) {
                @Override
                public void onNext(User response) {
                    super.onNext(response);
                    if (getView() != null) {
                        getView().navigateToProfile();
                    }
                }
            });
        }
    }

    private boolean isValid(String key) {
        if (key.contains(" ") || key.length() != 8) {
            if (getView() != null) getView().onError(getContext().getString(R.string.invalidKey));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValid(String login, String password, String passwordConfirm) {
        if (!CredentialUtils.isLoginValid(login)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.LOGIN);
        } else if (!CredentialUtils.isPasswordValid(password)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.PASSWORD);
        } else if (!CredentialUtils.isPasswordConfirmed(password, passwordConfirm)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.PASSWORD_CONFIRM);
        } else {
            return true;
        }
        return false;
    }


    private void start(User user) {

        final String wsuri = Constants.BASE_URL_SOCKET;

        Log.e(TAG, user.getToken());
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
                    .addHeader("Authorization", user.getToken())
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
