package com.peekaboo.presentation.comparators;

import com.peekaboo.domain.SmsDialog;

import java.util.Comparator;

/**
 * Created by st1ch on 19.10.2016.
 */

public class SmsDialogComparator implements Comparator<SmsDialog> {
    @Override
    public int compare(SmsDialog lhs, SmsDialog rhs) {
        return Long.valueOf(rhs.getLastMessage().getDate())
                .compareTo(lhs.getLastMessage().getDate());
    }
}
