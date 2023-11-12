<%@ page import="com.tictactoe.Sign" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Tic-Tac-Toe</title>
    <link href="static/main.css" rel="stylesheet">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script src="<c:url value="/static/jquery-3.6.0.min.js"/>"></script>
</head>
<body>
<h1>Tic-Tac-Toe</h1>
<table>
    <tr>
        <td onclick="window.location='/logic?click=0'">${data.get(0).getSign()}</td>
        <td onclick="window.location='/logic?click=1'">${data.get(1).getSign()}</td>
        <td onclick="window.location='/logic?click=2'">${data.get(2).getSign()}</td>
    </tr>
    <tr>
        <td onclick="window.location='/logic?click=3'">${data.get(3).getSign()}</td>
        <td onclick="window.location='/logic?click=4'">${data.get(4).getSign()}</td>
        <td onclick="window.location='/logic?click=5'">${data.get(5).getSign()}</td>
    </tr>
    <tr>
        <td onclick="window.location='/logic?click=6'">${data.get(6).getSign()}</td>
        <td onclick="window.location='/logic?click=7'">${data.get(7).getSign()}</td>
        <td onclick="window.location='/logic?click=8'">${data.get(8).getSign()}</td>
    </tr>


</table>
<c:choose>
    <c:when test="${sessionScope.winner eq Sign.CROSS}">
        <h1>You win</h1>
    </c:when>
    <c:when test="${sessionScope.winner eq Sign.NOUGHT}">
        <h1>You lose</h1>
    </c:when>
    <c:when test="${sessionScope.draw eq true}">
        <h1>Draw</h1>
        <button onclick="restart()">Start again</button>
    </c:when>
    <c:otherwise><h1>Playing</h1></c:otherwise>
</c:choose>
<script>
    function restart() {
        $.ajax({
            url: "/restart",
            type: 'POST',
            contentType:'application/json; charset=UTF-8',
            async:false,
            success:function () {
                location.reload()
            }
        })
    }
</script>
</body>