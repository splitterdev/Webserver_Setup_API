package org.webserver.page.serverPage;

import java.util.ArrayList;
import java.util.List;

import org.webserver.db.DataStorage;
import org.webserver.db.Query;
import org.webserver.db_object.Student;
import org.webserver.page.Page;
import org.webserver.page.PageBuilder;

@Page(
		pageId = "home",
		displayIdentifier = "page.actionDisplay.home"
)
public class HomePage extends PageBuilder {

	public static final String EXECUTED_ACTION_NAME = "EXECUTED_ACTION_NAME";

	@Override
	public void buildPage() throws Exception {
		setOutputObject("students", findStudents());
		setOutputObject("welcomeItemInfo", "Hello from page \"" + this.getClass().getName() + "\"!");
		setOutputObject("executedAction", getActionResult().getAttribute(EXECUTED_ACTION_NAME));
	}

	private List<Student> findStudents() {
		try {
			Query<Student> query = Query.select(Student.class);
			return DataStorage.getDataStorage().execute(query);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
