package com.peekaboo.presentation.comparators;

import com.peekaboo.domain.Sms;

import java.util.Comparator;

/**
 * Created by st1ch on 21.10.2016.
 */

public class SmsComparator implements Comparator<Sms> {
    @Override
    public int compare(Sms lhs, Sms rhs) {
        return Long.valueOf(lhs.getDate())
                .compareTo(rhs.getDate());
    }
}
