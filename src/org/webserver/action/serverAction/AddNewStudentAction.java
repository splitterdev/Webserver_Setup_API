package org.webserver.action.serverAction;

import java.util.Calendar;

import org.webserver.action.Action;
import org.webserver.action.ActionProcessor;
import org.webserver.action.ActionResult;
import org.webserver.db.DataStorage;
import org.webserver.db_object.Student;
import org.webserver.page.serverPage.HomePage;

@Action(
		actionId = "addNewStudent",
		actionPage = "home",
		displayIdentifier = "action.actionDisplay.addNewStudent"
)
public class AddNewStudentAction extends ActionProcessor {

	public AddNewStudentAction() {
	}

	@Override
	public ActionResult performAction() {
		ActionResult actionResult = new ActionResult();
		addStudent("Test-X", (long)(Math.random() * 20000.0));
		actionResult.setAttribute(HomePage.EXECUTED_ACTION_NAME, this.getActionName());
		return actionResult;
	}

	private void addStudent(String studentName, Long studentCardId) {
		Student student = new Student();
		Calendar calendar = Calendar.getInstance();
		student.setStudentName(studentName + " AT_" + calendar.getTimeInMillis());
		student.setStudentCardId(studentCardId);
		student.setStudentJoinDate(calendar.getTime());
		DataStorage.getDataStorage().create(student);
	}
}
