/**
 * 
 */
package it.dontesta.liferay.messagebus.example.mvc;

import java.io.IOException;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
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
	 * Send a list of no active user by SubscriptionSender
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 * @throws PortletException
	 * @throws SystemException
	 */
	public void sendEmailBySubscriptionSender(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException,
			PortletException, SystemException {

		User user = (User) actionRequest.getAttribute(WebKeys.USER);

		List<User> userList = getInactiveUsers(user);
		String emailBody = getEmailBody(userList);

		if (userList.size() > 0) {
			SubscriptionSender subscriptionSender = new SubscriptionSender();

			subscriptionSender.setSubject("List of disabled users");
			subscriptionSender.setBody(emailBody.toString());
			subscriptionSender.setUserId(user.getUserId());
			subscriptionSender.setCompanyId(user.getCompanyId());
			subscriptionSender.setFrom("noreply@liferay.com", "Liferay Portal");
			subscriptionSender.setHtmlFormat(false);
			subscriptionSender.setMailId("user", user.getUserId());

			subscriptionSender.addRuntimeSubscribers(user.getEmailAddress(),
					user.getFullName());

			List<EmailAddress> emails = (List<EmailAddress>) user
					.getEmailAddresses();
			if (emails.size() > 0) {
				if (_log.isInfoEnabled()) {
					_log.info("User " + user.getUserId()
							+ " has additional emails address");
				}
				for (EmailAddress emailAddress : emails) {
					subscriptionSender.addRuntimeSubscribers(
							emailAddress.getAddress(),
							(String) user.getFullName());

				}
			}

			subscriptionSender.flushNotificationsAsync();
			SessionMessages.add(actionRequest,
					"email-userlist-no-active-send-successfully");
		} else {
			SessionErrors.add(actionRequest,
					"email-userlist-no-active-count-zero");
		}
	}

	/**
	 * Send a list of no active user by Email Sender
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 * @throws PortletException
	 * @throws SystemException
	 */
	public void sendEmailByEmailSender(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException,
			PortletException, SystemException {
		User user = (User) actionRequest.getAttribute(WebKeys.USER);

		List<User> userList = getInactiveUsers(user);
		String emailBody = getEmailBody(userList);

		if (userList.size() > 0) {
			InternetAddress from = new InternetAddress("noreply@liferay.com",
					"Liferay Portale");
			InternetAddress to = new InternetAddress(user.getEmailAddress(),
					user.getFullName());

			MailMessage message = new MailMessage(from, to,
					"List of disabled users", emailBody, false);
			
			Message myMessage = new Message();
			myMessage.setDestinationName(DestinationNames.MAIL);
			myMessage.setPayload(message);
			MessageBusUtil.sendMessage(myMessage.getDestinationName(), myMessage);

			SessionMessages.add(actionRequest,
					"email-userlist-no-active-send-successfully");
		} else {
			SessionErrors.add(actionRequest,
					"email-userlist-no-active-count-zero");
		}

	}

	/**
	 * 
	 * @param userList
	 * @return
	 */
	protected String getEmailBody(List<User> userList) {
		StringBundler emailBody = new StringBundler();

		emailBody.append("Below is the list of disabled users:");
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

		return emailBody.toString();

	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected List<User> getInactiveUsers(User user) throws SystemException {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil
				.forClass(User.class);
		dynamicQuery.add(PropertyFactoryUtil.forName("status").eq(
				WorkflowConstants.STATUS_INACTIVE));
		dynamicQuery.add(PropertyFactoryUtil.forName("companyId").eq(
				user.getCompanyId()));

		List<User> userList = UserLocalServiceUtil.dynamicQuery(dynamicQuery);
		return userList;
	}

	private static Log _log = LogFactoryUtil.getLog(SendEmail.class);
}
