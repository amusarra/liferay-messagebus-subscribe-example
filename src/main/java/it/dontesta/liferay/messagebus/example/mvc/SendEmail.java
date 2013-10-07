/**
 * 
 */
package it.dontesta.liferay.messagebus.example.mvc;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.SubscriptionSender;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author amusarra
 *
 */
public class SendEmail extends MVCPortlet {

	/**
	 * Send a list of no active user
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 * @throws PortletException
	 * @throws SystemException
	 */
	public void sendUserListNoActive(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException, SystemException {

		User user = (User) actionRequest.getAttribute(WebKeys.USER);
		StringBundler emailBody = new StringBundler();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(User.class);
		dynamicQuery.add(PropertyFactoryUtil.forName("status").eq(WorkflowConstants.STATUS_INACTIVE));
		dynamicQuery.add(PropertyFactoryUtil.forName("companyId").eq(user.getCompanyId()));
		
		List<User> userList = UserLocalServiceUtil.dynamicQuery(dynamicQuery);
		
		emailBody.append("A seguire la lista degli utenti disattivati:");
		emailBody.append(StringPool.NEW_LINE);
		
		for (User detailUser : userList) {
			emailBody.append(StringPool.POUND);
			emailBody.append(StringPool.SPACE);
			emailBody.append(detailUser.getFullName());
			emailBody.append(StringPool.SPACE);
			emailBody.append(" last login on: ");
			emailBody
					.append((Validator.isNotNull(detailUser.getLastLoginDate())) ? detailUser
							.getLastLoginDate() : StringPool.BLANK);
			emailBody.append(StringPool.NEW_LINE);
		}
		
		if (userList.size() > 0) {
			SubscriptionSender subscriptionSender = new SubscriptionSender();

			subscriptionSender.setSubject("Lista degli utenti disattivati");
			subscriptionSender.setBody(emailBody.toString());
			subscriptionSender.setUserId(user.getUserId());
			subscriptionSender.setCompanyId(user.getCompanyId());
			subscriptionSender.setFrom("noreply@liferay.com", "Liferay Portal");
			subscriptionSender.setHtmlFormat(false);
			subscriptionSender.setMailId("user", user.getUserId());

			subscriptionSender.addRuntimeSubscribers(
				user.getEmailAddress(),
				user.getFullName());

			List<EmailAddress> emails = (List<EmailAddress>) user.getEmailAddresses();
			if (emails.size() > 0) {
				if (_log.isInfoEnabled()) {
					_log.info("User " + user.getUserId() +
						" has additional emails address");
				}
				for (EmailAddress emailAddress : emails) {
					subscriptionSender.addRuntimeSubscribers(
						emailAddress.getAddress(),
						(String) user.getFullName());
					
				}
			}
			
			subscriptionSender.flushNotificationsAsync();

			SessionMessages.add(actionRequest, "email-userlist-no-active-send-successfully");
		} else {
			SessionErrors.add(actionRequest, "email-userlist-no-active-count-zero");
		}
	}
	
	private static Log _log =
			LogFactoryUtil.getLog(SendEmail.class);
}
