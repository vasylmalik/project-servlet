package com.tictactoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {

    private static final String PATH_INDEX = "WEB-INF/index.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        Field field = extractField(currentSession);

        if (checkWin(req, resp, currentSession, field)) {
            return;
        }

        int index = getSelectedIndex(req);
        Sign currentSign = field.getField().get(index);
        if (Sign.EMPTY != currentSign) {
            req.getRequestDispatcher(PATH_INDEX)
                    .forward(req, resp);
            return;
        }
        field.getField().put(index, Sign.CROSS);
        if (checkWin(req, resp, currentSession, field)) {
            return;
        }

        int emptyFieldIndex = field.getEmptyFieldIndex();
        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            if (checkWin(req, resp, currentSession, field)) {
                return;
            }
        } else {
            currentSession.setAttribute("draw", true);
            List<Sign> data = field.getFieldData();
            currentSession.setAttribute("data", data);
            req.getRequestDispatcher(PATH_INDEX)
                    .forward(req, resp);
            return;
        }

        List<Sign> data = field.getFieldData();
        currentSession.setAttribute("data", data);
        req.getRequestDispatcher(PATH_INDEX)
                .forward(req, resp);
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }

    private boolean checkWin(HttpServletRequest req,
                             HttpServletResponse resp,
                             HttpSession currentSession,
                             Field field) throws IOException, ServletException {
        Sign winner = field.checkWin();
        if (winner != Sign.EMPTY) {
            currentSession.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            currentSession.setAttribute("data", data);
            req.getRequestDispatcher(PATH_INDEX)
                    .forward(req, resp);
            return true;
        }
        return false;
    }
}
