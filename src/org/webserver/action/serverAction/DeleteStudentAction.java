package org.webserver.action.serverAction;

import java.util.List;

import org.webserver.action.Action;
import org.webserver.action.ActionProcessor;
import org.webserver.action.ActionResult;
import org.webserver.db.DataStorage;
import org.webserver.db.Query;
import org.webserver.db.QueryException;
import org.webserver.db.WhereCompare;
import org.webserver.db_object.Student;
import org.webserver.page.serverPage.HomePage;

@Action(
		actionId = "deleteStudent",
		actionPage = "home",
		displayIdentifier = "action.actionDisplay.deleteStudent"
)
public class DeleteStudentAction extends ActionProcessor {

	public DeleteStudentAction() {
	}

	@Override
	public ActionResult performAction() {
		ActionResult actionResult = new ActionResult();
		Long studentId = Long.parseLong(getRequest().getParameter("studentId"));
		int updateCount = 0;
		try {
			updateCount = deleteStudent(studentId);
		} catch (QueryException e) {
			e.printStackTrace();
		}
		actionResult.setAttribute(HomePage.EXECUTED_ACTION_NAME, this.getActionName() + "(" + updateCount + ")");
		return actionResult;
	}

	private int deleteStudent(Long id) throws QueryException {
		Query<Student> query = Query.select(Student.class);
		query.appendWhere(Student.class, "id", WhereCompare.EQUAL, id);
		List<Student> students = DataStorage.getDataStorage().execute(query);
		for (Student student : students) {
			DataStorage.getDataStorage().delete(student);
		}
		return students.size();
	}
}
