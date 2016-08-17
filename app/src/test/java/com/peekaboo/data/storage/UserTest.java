package com.peekaboo.data.storage;

import android.content.SharedPreferences;

import com.peekaboo.domain.AccountUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserTest {
    @Mock
    SharedPreferences preferences;
    @Mock
    SharedPreferences.Editor editor;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putString(any(String.class), any(String.class))).thenReturn(editor);
    }

    @Test
    public void whenUserHasTokenAndIdThenIsAuthorizedReturnTrue() {
        AccountUser user = new AccountUser(preferences);
        user.saveId("id");
        user.saveToken("token");

        assertEquals(user.isAuthorized(), true);
        verify(editor, times(1)).putString(AccountUser.ID, "id");
        verify(editor, times(1)).putString(AccountUser.TOKEN, "token");
        verify(editor, times(2)).commit();
    }

    @Test
    public void whenUserHasNoTokenAndIdThenIsAuthorizedReturnFalse() {
        AccountUser user = new AccountUser(preferences);

        assertEquals(user.isAuthorized(), false);
    }

    @Test
    public void whenUserHasTokenThenIsAuthorizedReturnFalse() {
        AccountUser user = new AccountUser(preferences);
        user.saveToken("token");

        assertEquals(user.isAuthorized(), false);
        verify(editor, times(1)).putString(AccountUser.TOKEN, "token");
        verify(editor, times(1)).commit();
    }

    @Test
    public void whenUserHasIdThenIsAuthorizedReturnFalse() {
        AccountUser user = new AccountUser(preferences);
        user.saveId("id");

        assertEquals(user.isAuthorized(), false);
        verify(editor, times(1)).putString(AccountUser.ID, "id");
        verify(editor, times(1)).commit();
    }

    @Test
    public void whenUserCreatedWithStoredDataThenRestoreDataAndAuthorizedReturnTrue() {
        when(preferences.getString(AccountUser.ID, null)).thenReturn("id");
        when(preferences.getString(AccountUser.TOKEN, null)).thenReturn("token");

        AccountUser user = new AccountUser(preferences);

        assertEquals(user.getId(), "id");
        assertEquals(user.getToken(), "token");
        assertEquals(user.isAuthorized(), true);
    }
}
