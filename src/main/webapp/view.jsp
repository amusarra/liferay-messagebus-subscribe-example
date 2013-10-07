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

<%@ include file="/init.jsp"%>

<portlet:actionURL name="sendUserListNoActive" var="sendUserListNoActive"/>
<liferay-ui:success key="email-userlist-no-active-send-successfully" message="email-userlist-no-active-send-successfully" />
<liferay-ui:error key="email-userlist-no-active-count-zero" message="email-userlist-no-active-count-zero" />

This is the <b>liferay-messagebus-subscribe-example</b>.

<aui:form name="fmsendUserListNoActive"
		action="<%=sendUserListNoActive.toString()%>" method="post">
		<aui:fieldset>
			<aui:button-row>
				<aui:button value="Send List of No Active User" type="submit" />
			</aui:button-row>
		</aui:fieldset>
		
</aui:form>
