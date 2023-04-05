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
		actionId = "updateStudent",
		actionPage = "home",
		displayIdentifier = "action.actionDisplay.updateStudent"
)
public class UpdateStudentAction extends ActionProcessor {

	public UpdateStudentAction() {
	}

	@Override
	public ActionResult performAction() {
		ActionResult actionResult = new ActionResult();
		Long studentId = Long.parseLong(getRequest().getParameter("studentId"));
		int updateCount = 0;
		try {
			updateCount = updateStudent(studentId, (long)(Math.random() * 20000.0));
		} catch (QueryException e) {
			e.printStackTrace();
		}
		actionResult.setAttribute(HomePage.EXECUTED_ACTION_NAME, this.getActionName() + "(" + updateCount + ")");
		return actionResult;
	}

	private int updateStudent(Long id, Long studentCardId) throws QueryException {
		Query<Student> query = Query.select(Student.class);
		query.appendWhere(Student.class, "id", WhereCompare.EQUAL, id);
		List<Student> students = DataStorage.getDataStorage().execute(query);
		for (Student student : students) {
			student.setStudentCardId(studentCardId);
			DataStorage.getDataStorage().update(student);
		}
		return students.size();
	}
}
