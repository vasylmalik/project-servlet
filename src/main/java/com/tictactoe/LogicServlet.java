package com.tictactoe;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Field field = (Field) session.getAttribute("field");
        int index = getSelectedIndex(request);
        Sign currentSign = field.getField().get(index);
        if (currentSign != Sign.EMPTY) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        }
        field.getField().put(index, Sign.CROSS);

        int emptyFieldIndex = field.getEmptyFieldIndex();

        if(checkWin(response,session,field)) {
            return;
        }

        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex,Sign.NOUGHT);
            if(checkWin(response,session,field)) {
                return;
            }
        } else {
            session.setAttribute("draw", true);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);
            response.sendRedirect("/index.jsp");
            return;
        }

        List<Sign> data = field.getFieldData();
        session.setAttribute("data", data);
        session.setAttribute("field", field);
        response.sendRedirect("/index.jsp");
    }

    private boolean checkWin (HttpServletResponse response, HttpSession session, Field field) throws IOException {
        Sign winner = field.checkWin();
        if(winner == Sign.CROSS || winner == Sign.NOUGHT) {
            session.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);
            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }
}
