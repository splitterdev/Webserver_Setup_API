package org.webserver.info;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

import org.webserver.config.LocalizedString;
import org.webserver.page.Page;
import org.webserver.page.PageMapper;

public class PageInfo {

	public static PageInfo getPageInfo(String pageId, Locale locale, Map<?, ?> parameterMap) {
		return new PageInfo(pageId, locale, parameterMap);
	}

	private String pageId;
	private Locale locale;
	private Map<?, ?> parameterMap;

	private PageInfo(String pageId, Locale locale, Map<?, ?> parameterMap) {
		this.pageId = pageId;
		this.locale = locale;
		this.parameterMap = parameterMap;
	}

	public String getName() {
		try {
			Page pageInfo = PageMapper.getPageInfo(pageId);
			if (pageInfo != null) {
				return LocalizedString.get(pageInfo.displayIdentifier(), locale);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "PAGE_NAME";
	}
	
	public String getReferencedParameterMap() throws UnsupportedEncodingException {
		if (parameterMap == null || parameterMap.isEmpty()) {
			return "?action=home"; // default action redirect
		}
		StringBuilder builder = new StringBuilder("?");
		boolean firstAdd = true;
		for (Map.Entry<?, ?> entry : parameterMap.entrySet()) {
			Object paramName = entry.getKey();
			Object paramValue = entry.getValue();
			if (paramName != null && paramValue != null) {
				if (firstAdd) {
					firstAdd = false;
				} else {
					builder.append("&");
				}
				String keyEncoded = URLEncoder.encode(paramName.toString(), "UTF-8");
				String valueEncoded = null;
				if (paramValue instanceof Object[]) {
					Object[] multiValue = (Object[]) paramValue;
					StringBuilder subBuild = null;
					for (Object o : multiValue) {
						if (subBuild == null) {
							subBuild = new StringBuilder(URLEncoder.encode(o.toString(), "UTF-8"));
						} else {
							subBuild.append(",");
							subBuild.append(URLEncoder.encode(o.toString(), "UTF-8"));
						}
					}
					if (subBuild != null) {
						valueEncoded = subBuild.toString();
					}
				} else {
					valueEncoded = URLEncoder.encode(paramValue.toString(), "UTF-8");
				}
				if (keyEncoded != null && valueEncoded != null) {
					builder.append(keyEncoded);
					builder.append("=");
					builder.append(valueEncoded);
				}
			}
		}
		return builder.toString();
	}

}
