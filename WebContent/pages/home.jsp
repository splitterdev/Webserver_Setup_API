<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<%@include file="/WEB-INF/pageFrags/pageBegin.jsp"%>
	<p>Testing main page</p>
	<p>Response from page: <c:out value='${welcomeItemInfo}'/></p>
	<p>
		<c:forEach var="student" items="${students}">
			<c:out value='${student.id}'/> | <c:out value='${student.studentName}'/> | <c:out value='${student.studentCardId}'/> | <c:out value='${student.studentJoinDate}'/>
			||| <a href="/?action=updateStudent&studentId=<c:out value='${student.id}'/>">Update</a>
			||| <a href="/?action=deleteStudent&studentId=<c:out value='${student.id}'/>">Delete</a>
			<br/>
		</c:forEach>
	</p>
	<p>Executed action: <c:out value='${executedAction}'/></p>
	<p>
		<a href="/?action=addNewStudent">Add new student</a>
	</p>
	<%@include file="/WEB-INF/pageFrags/pageEnd.jsp"%>
</html>
