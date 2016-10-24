package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.Sms;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 17.10.2016.
 */

public class GetContactSmsUseCase extends UseCase<List<Sms>> {

    private SessionRepository sessionRepository;
    private String phoneNumber;

    @Inject
    public GetContactSmsUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    @Override
//    protected Observable<List<Sms>> getUseCaseObservable() {
//        return sessionRepository.getAllSmsList().map(smses -> {
//            List<Sms> newList = new ArrayList<>();
//            for(Sms sms: smses){
//                if(sms.getAddress().equals(phoneNumber)){
//                    newList.add(sms);
//                }
//            }
//            return newList;
//        });
//    }

    @Override
    protected Observable<List<Sms>> getUseCaseObservable() {
        return sessionRepository.getContactSmsList(phoneNumber);
    }
}
