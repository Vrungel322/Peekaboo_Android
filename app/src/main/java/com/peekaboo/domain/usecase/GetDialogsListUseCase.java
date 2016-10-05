package com.peekaboo.domain.usecase;

import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 27.09.2016.
 */

public class GetDialogsListUseCase extends UseCase<List<Dialog>> {

    private SessionRepository sessionRepository;

    @Inject
    public GetDialogsListUseCase(SubscribeOn subscribeOn,
                                 ObserveOn observeOn,
                                 SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<List<Dialog>> getUseCaseObservable() {
        return sessionRepository.loadDialogs()
                .flatMap(Observable::from)
                .flatMap(dialog -> {
                    String contactId = dialog.getContact().contactId();
                    int unreadMessagesCount = sessionRepository.getUnreadMessagesCount(contactId).toBlocking().first();
                    dialog.setUnreadMessagesCount(unreadMessagesCount);
                    return Observable.just(dialog);
                }).toList()
                ;
    }
}
