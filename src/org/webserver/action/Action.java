package org.webserver.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	String actionId();
	String actionPage();
	String displayIdentifier();
}
