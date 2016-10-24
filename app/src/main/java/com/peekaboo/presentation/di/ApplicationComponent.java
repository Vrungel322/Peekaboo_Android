package com.peekaboo.presentation.di;

import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.presentation.activities.ChatActivity;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.activities.SplashActivity;
import com.peekaboo.presentation.dialogs.AvatarChangeDialog;
import com.peekaboo.presentation.dialogs.ConfirmSignUpDialog;
import com.peekaboo.presentation.dialogs.RecordDialogFragment;
import com.peekaboo.presentation.fragments.ChatFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.MessangerTestFragment;
import com.peekaboo.presentation.fragments.PreviewImageFragment;
import com.peekaboo.presentation.fragments.SmsChatFragment;
import com.peekaboo.presentation.fragments.SmsDialogsFragment;
import com.peekaboo.presentation.services.NotificationService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    PeekabooApi api();

    //    Inject of Activities
    void inject(SplashActivity splashActivity);

    void inject(LogInActivity logInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(MainActivity mainActivity);

    void inject(ChatActivity chatActivity);

    //    Inject of Fragments
    void inject(ContactsFragment contactsFragment);

    void inject(ConfirmSignUpDialog confirmSignUpDialog);

    void inject(AvatarChangeDialog avatarChangeDialog);

    void inject(DialogsFragment dialogsFragment);

    void inject(SmsDialogsFragment smsDialogsFragment);

    void inject(SmsChatFragment smsChatFragment);

    void inject(ChatFragment chatFragment);

    void inject(RecordDialogFragment recordDialogFragment);

    void inject(PreviewImageFragment previewImageFragment);

    //    Inject of Services
    void inject(NotificationService notificationService);

    //Tests
    void inject(MessangerTestFragment messangerTestFragment);
}
