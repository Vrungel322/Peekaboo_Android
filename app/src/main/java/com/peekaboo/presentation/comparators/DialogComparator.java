package com.peekaboo.presentation.comparators;

import com.peekaboo.domain.Dialog;

import java.util.Comparator;

/**
 * Created by st1ch on 28.09.2016.
 */

public class DialogComparator implements Comparator<Dialog> {
    @Override
    public int compare(Dialog lhs, Dialog rhs) {
        return Long.valueOf(rhs.getLastMessage().timestamp())
                .compareTo(lhs.getLastMessage().timestamp());
    }
}
