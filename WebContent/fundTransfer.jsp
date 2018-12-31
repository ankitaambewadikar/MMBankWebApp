<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Fund Transfer</title>
</head>
<body>
	<h2>fund Transfer</h2>
	<form action="fund.mm">
		Enter Account Number of Sender : <input type="text"
			name="senderAccountNumber"><br>
		<br> Enter Account Number of Receiver : <input type="text"
			name="receiverAccountNumber"><br>
		<br> Enter Amount to Transfer:<input type=number
			name="amountToTransfer"><br>
		<br> <input type="submit" value="Submit">
	</form>
	<div>
		<jsp:include page="homeLink.html"></jsp:include>
	</div>
</body>
</html>