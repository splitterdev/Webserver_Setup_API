package org.webserver.action.serverAction;

import org.webserver.action.Action;
import org.webserver.action.ActionProcessor;
import org.webserver.action.ActionResult;
import org.webserver.page.serverPage.HomePage;

@Action(
		actionId = "home",
		actionPage = "home",
		displayIdentifier = "action.actionDisplay.home"
)
public class HomeAction extends ActionProcessor {

	public HomeAction() {
	}

	public static final String ATTRIBUTE_STUDENTS = "attribute_students";

	@Override
	public ActionResult performAction() {
		ActionResult actionResult = new ActionResult();
		actionResult.setAttribute(HomePage.EXECUTED_ACTION_NAME, this.getActionName());
		return actionResult;
	}

}
