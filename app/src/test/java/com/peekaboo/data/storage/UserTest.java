package com.peekaboo.data.storage;

import android.content.SharedPreferences;

import com.peekaboo.domain.User;

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
        User user = new User(preferences);
        user.saveId("id");
        user.saveToken("token");

        assertEquals(user.isAuthorized(), true);
        verify(editor, times(1)).putString(User.ID, "id");
        verify(editor, times(1)).putString(User.TOKEN, "token");
        verify(editor, times(2)).commit();
    }

    @Test
    public void whenUserHasNoTokenAndIdThenIsAuthorizedReturnFalse() {
        User user = new User(preferences);

        assertEquals(user.isAuthorized(), false);
    }

    @Test
    public void whenUserHasTokenThenIsAuthorizedReturnFalse() {
        User user = new User(preferences);
        user.saveToken("token");

        assertEquals(user.isAuthorized(), false);
        verify(editor, times(1)).putString(User.TOKEN, "token");
        verify(editor, times(1)).commit();
    }

    @Test
    public void whenUserHasIdThenIsAuthorizedReturnFalse() {
        User user = new User(preferences);
        user.saveId("id");

        assertEquals(user.isAuthorized(), false);
        verify(editor, times(1)).putString(User.ID, "id");
        verify(editor, times(1)).commit();
    }

    @Test
    public void whenUserCreatedWithStoredDataThenRestoreDataAndAuthorizedReturnTrue() {
        when(preferences.getString(User.ID, null)).thenReturn("id");
        when(preferences.getString(User.TOKEN, null)).thenReturn("token");

        User user = new User(preferences);

        assertEquals(user.getId(), "id");
        assertEquals(user.getToken(), "token");
        assertEquals(user.isAuthorized(), true);
    }
}
