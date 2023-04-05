package org.webserver.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webserver.action.serverAction.AddNewStudentAction;
import org.webserver.action.serverAction.DeleteStudentAction;
import org.webserver.action.serverAction.HomeAction;
import org.webserver.action.serverAction.UpdateStudentAction;

public class ActionMapper {

	private static Map<String, Class<? extends ActionProcessor>> actionClasses = new HashMap<>();

	private static final String DEFAULT_ACTION = "home";

	public static ActionResult call(String action, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Class<? extends ActionProcessor> actionClass = actionClasses.get(action);
		if (actionClass == null) {
			actionClass = actionClasses.get(DEFAULT_ACTION);
			action = DEFAULT_ACTION;
		}
		if (actionClass != null) {
			try {
				ActionProcessor processor = actionClass.newInstance();
				if (processor != null) {
					processor.setHttpData(request, response);
					processor.setActionName(action);
					ActionResult actionResult = processor.performAction();
					Action actionAnnotation = actionClass.getAnnotation(Action.class);
					if (actionResult == null) {
						actionResult = new ActionResult(actionAnnotation.actionPage());
					} else if (actionResult.getForwardPage() == null) {
						actionResult.setForwardPage(actionAnnotation.actionPage());
					}
					if (actionResult.getAction() == null) {
						actionResult.setAction(action);
					}
					return actionResult;
				}
			} catch (Exception e) {
				System.out.println("Cannot call action: " + action + " (class " + actionClass.getName() + ")");
				e.printStackTrace();
			}
		}
		throw new Exception("No action: " + action);
	}

	public static Action getActionInfo(String action) {
		Class<? extends ActionProcessor> actionClass = actionClasses.get(action);
		if (actionClass != null) {
			return actionClass.getAnnotation(Action.class);
		}
		return null;
	}

	public static void mapAction(Class<? extends ActionProcessor> actionClass) throws Exception {
		if (actionClass != null) {
			Action actionInfo = actionClass.getAnnotation(Action.class);
			if (actionInfo != null) {
				actionClasses.put(actionInfo.actionId(), actionClass);
				return;
			} else {
				throw new Exception("Class " + actionClass.getName() + " is not annotated by @Action interface");
			}
		}
		throw new Exception("Null action");
	}

	static {
		try {
			mapAction(HomeAction.class);
			mapAction(AddNewStudentAction.class);
			mapAction(UpdateStudentAction.class);
			mapAction(DeleteStudentAction.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
