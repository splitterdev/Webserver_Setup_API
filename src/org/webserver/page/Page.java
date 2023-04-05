package org.webserver.page;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Page {
	String pageId();
	String displayIdentifier();
}
