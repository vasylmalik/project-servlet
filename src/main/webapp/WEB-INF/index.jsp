<%@ page import="com.tictactoe.Sign" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tic-Tac-Toe</title>
    <link href="../static/main.css" rel="stylesheet">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script src="<c:url value="/static/jquery-3.6.0.min.js"/>"></script>
</head>
<body>
<div class="game-container">
    <h1>Tic-Tac-Toe</h1>
    <div class="board">
        <c:forEach var="sign" items="${data}" varStatus="status">
            <c:choose>
                <c:when test="${sign == Sign.CROSS}">
                    <div class="cell x">X</div>
                </c:when>
                <c:when test="${sign == Sign.NOUGHT}">
                    <div class="cell o">O</div>
                </c:when>
                <c:otherwise>
                    <div class="cell" onclick="window.location='/logic?click=${status.index}'"></div>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
    <c:choose>
        <c:when test="${draw}">
            <div class="controls" id="endGameUI">
                <div id="status">
                    IT'S A DRAW
                </div>
                <button class="restart-btn" onclick="restart()">Start again</button>
            </div>
        </c:when>
        <c:when test="${winner == Sign.CROSS}">
            <div class="controls" id="endGameUI">
                <div id="status">
                    CROSSES WIN!
                </div>
                <button class="restart-btn" onclick="restart()">Start again</button>
            </div>
        </c:when>
        <c:when test="${winner == Sign.NOUGHT}">
            <div class="controls" id="endGameUI">
                <div id="status">
                    NOUGHTS WIN!
                </div>
                <button class="restart-btn" onclick="restart()">Start again</button>
            </div>
        </c:when>
        <c:otherwise>
            <div class="controls">
                <div id="status">
                    MESSAGE
                </div>
                <button class="restart-btn">Button</button>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script>
    function restart() {
        $.get('/restart', function (data, status) {
            if (status === 'success') {
                window.location.href = '/';
            } else {
                alert('An error occurred: ${status}');
            }
        });
    }

    <c:if test="${(winner != null) || draw}">
    document.getElementById('endGameUI').classList.add('show');
    </c:if>
</script>
</body>
</html>