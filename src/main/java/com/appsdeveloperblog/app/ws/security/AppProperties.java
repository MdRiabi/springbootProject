package com.appsdeveloperblog.app.ws.security;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
    private Environment env;

    public String getTockenString(){

        return env.getProperty("tokenSecret");
    
    }
}
