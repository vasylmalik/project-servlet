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
        HttpSession currSession = req.getSession();
        Field field = extractField(currSession);

        int index = getSelectedIndex(req);
        Sign currentSign = field.getField().get(index);

        if (Sign.EMPTY != currentSign){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        field.getField().put(index, Sign.CROSS);

        if (checkWin(resp, currSession, field)) {
            return;
        }

        int emptyFieldIndex = field.getEmptyFieldIndex();

        if (emptyFieldIndex >= 0){
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            if (checkWin(resp, currSession, field)) {
                return;
            }
        } else{
            currSession.setAttribute("draw", true);

            List<Sign> data = field.getFieldData();

            currSession.setAttribute("data", data);

            resp.sendRedirect("/index.jsp");
            return;
        }

        List<Sign> data = field.getFieldData();

        currSession.setAttribute("data", data);
        currSession.setAttribute("field", field);

        resp.sendRedirect("/index.jsp");
    }

    private int getSelectedIndex(HttpServletRequest request){
        String click = request.getParameter("click");
        boolean isDigit = click.chars().allMatch(Character::isDigit);
        return isDigit ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession currentSession){
        Object fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }

    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            currentSession.setAttribute("winner", winner);

            List<Sign> data = field.getFieldData();

            currentSession.setAttribute("data", data);

            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }
}
