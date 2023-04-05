<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title>Server Error</title>
		<style><%@include file="/WEB-INF/styles/main.css"%></style>
	</head>
	<body>
		<div>
			<table border="0">
				<tr>
					<td>
						<h1>TEST</h1>
					</td>
				</tr>
			</table>
			<table class="centered" style="position:relative; width:80%; max-width:1280px; height:auto; text-align:center">
				<div class="centered">
					<img src='<c:url value="/images/error.jpg"></c:url>' style="width:30%; max-width:600px"/>
					<h1>General error</h1>
					<h3>An error occurred. Contact the administrator if you believe you didn't break anything.</h3>
				</div>
			</table>
		</div>
		<%@include file="/WEB-INF/pageFrags/footer.jsp"%>
	</body>
</html>