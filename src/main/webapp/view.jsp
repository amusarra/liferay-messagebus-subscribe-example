<%--
  #%L
  Liferay Message Bus Subscription Sender Example Portlet
  %%
  Copyright (C) 2013 Antonio Musarra
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the 
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public 
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ include file="/init.jsp"%>

<portlet:actionURL name="sendEmailBySubscriptionSender" var="sendEmailBySubscriptionSender"/>
<portlet:actionURL name="sendEmailByEmailSender" var="sendEmailByEmailSender"/>

<liferay-ui:success key="email-userlist-no-active-send-successfully" message="email-userlist-no-active-send-successfully" />
<liferay-ui:error key="email-userlist-no-active-count-zero" message="email-userlist-no-active-count-zero" />

This is the <b>Liferay Message Bus-Subscription Sender example</b>.

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