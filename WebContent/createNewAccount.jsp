<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add New Account</title>
</head>
<body>
<h2>Add New Account</h2>
<form action="addNewAccount.mm">
Enter AccountHolderName:<input type="text" name="accountHolderName" maxlength="30"><br>
Enter Initial Account Balance:<input type=number name="accountBalance"><br>
Enter Salaried y/n:<input type="radio" name="isSalaried" value="Yes">Yes
					<input type="radio" name="isSalaried" value="No">No<br>
<input type="submit" value="Submit">
<input type="reset" value="Reset">
</form>
<div>
		<jsp:include page="homeLink.html"></jsp:include>
	</div>
</body>
</html>