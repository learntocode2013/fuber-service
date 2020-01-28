package com.quintype.fuberservice.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/fuber")
@ApplicationScoped
public class FuberApplication extends Application {
}
