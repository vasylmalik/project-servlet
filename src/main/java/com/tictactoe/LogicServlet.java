package com.tictactoe;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = getIndex(req);

        HttpSession session = req.getSession();
        Field field = getField(session);

        if (field.getField().get(index) != Sign.EMPTY) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
        }
        field.getField().put(index, Sign.CROSS);

        if (checkWin(resp, session, field)) {
            return;
        }

        int emptyNought = field.getEmptyFieldIndex();
        if (emptyNought >= 0) {
            field.getField().put(emptyNought, Sign.NOUGHT);

            if (checkWin(resp, session, field)) {
                return;
            }
        } else {
            session.setAttribute("draw", true);

            List<Sign> data = field.getFieldData();

            session.setAttribute("data", data);

            resp.sendRedirect("/index.jsp");
            return;
        }

        List<Sign> data = field.getFieldData();

        session.setAttribute("field", field);
        session.setAttribute("data", data);

        resp.sendRedirect("/index.jsp");
    }

    private int getIndex(HttpServletRequest request) {
        String num = request.getParameter("click");
        boolean isDigit = num.chars().allMatch(Character::isDigit);
        return isDigit ? Integer.parseInt(num) : 0;
    }

    private Field getField(HttpSession session) {
        Object field = session.getAttribute("field");
        if (Field.class != field.getClass()) {
            session.invalidate();
            throw new RuntimeException("Sorry! Session is broken");
        }
        return (Field) field;
    }

    private boolean checkWin(HttpServletResponse resp, HttpSession session, Field field) throws IOException {
        Sign winner = field.checkWin();

        if (winner == Sign.CROSS || winner == Sign.NOUGHT) {
            session.setAttribute("winner", winner);

            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);

            resp.sendRedirect("index.jsp");

            return true;
        }
        return false;
    }
}
