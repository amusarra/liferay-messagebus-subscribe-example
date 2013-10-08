<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ include file="/init.jsp"%>

<portlet:actionURL name="sendEmailBySubscriptionSender" var="sendEmailBySubscriptionSender"/>
<portlet:actionURL name="sendEmailByEmailSender" var="sendEmailByEmailSender"/>

<liferay-ui:success key="email-userlist-no-active-send-successfully" message="email-userlist-no-active-send-successfully" />
<liferay-ui:error key="email-userlist-no-active-count-zero" message="email-userlist-no-active-count-zero" />

This is the <b>liferay-messagebus-subscribe-example</b>.

<aui:form name="<portlet:namespace/>fmSendEmailBySubscriptionSender"
		action="<%=sendEmailBySubscriptionSender.toString()%>" method="post">
		<aui:fieldset>
			<aui:button-row>
				<aui:button  value='<%=LanguageUtil.get(portletConfig, locale, "send-email-by-subscriptionsender") %>' type="submit" />
			</aui:button-row>
		</aui:fieldset>
		
</aui:form>

<aui:form name="<portlet:namespace/>fmSendEmailByEmailSender"
		action="<%=sendEmailByEmailSender.toString()%>" method="post">
		<aui:fieldset>
			<aui:button-row>
				<aui:button  value='<%=LanguageUtil.get(portletConfig, locale, "send-email-by-email-sender") %>' type="submit" />
			</aui:button-row>
		</aui:fieldset>
</aui:form>