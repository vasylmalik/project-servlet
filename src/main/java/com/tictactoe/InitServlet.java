package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //создаем новую сессию
        HttpSession currentSession = req.getSession(true);

        Field field = new Field();
        Map<Integer, Sign> fieldData = field.getField();

        List<Sign> data = field.getFieldData();

        currentSession.setAttribute("field", field);

        currentSession.setAttribute("data", data);
        //теперь данные сохраняются в сессии (внутри сессии есть атрибуты, ID ее) между запросами
        req.getRequestDispatcher("/index.jsp").forward(req, resp);

    }
}
