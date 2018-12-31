<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Check Current Balance</title>
</head>
<body>
	<h2>Check current Balance of Account</h2>
	<form action="currentBalance.mm">
		Enter Account Number:<input type="text" name="accountNumber"><br>

		<input type="submit" value="Find">

	</form>
	<div>
		<jsp:include page="homeLink.html"></jsp:include>
	</div>
</body>
</html>