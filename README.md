Liferay Message Bus Subscribe Example Portlet
=====================================
Welcome dear readers!

This sample portlet shows how easy it is to use the Liferay Message Bus to send email notifications. The portlet example uses the class SubscriptionSender, this class then uses the Message Bus to send the email (using the destination liferay/subscription_sender).
Clone repository and build the package

	$ cd /tmp
	$ git clone https://github.com/amusarra/liferay-messagebus-subscribe-example.git
	$ mvn package