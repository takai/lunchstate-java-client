package com.herokuapp.lunchstate.client;

import com.google.inject.AbstractModule;
import com.herokuapp.lunchstate.client.service.GoogleAuthorizationService;
import com.herokuapp.lunchstate.client.service.LunchstateService;
import com.herokuapp.lunchstate.client.service.impl.GoogleAuthorizationServiceImpl;
import com.herokuapp.lunchstate.client.service.impl.LunchstateServiceImpl;

public class LunchstateClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GoogleAuthorizationService.class).to(GoogleAuthorizationServiceImpl.class);
        bind(LunchstateService.class).to(LunchstateServiceImpl.class);
    }
}
