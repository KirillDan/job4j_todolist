package ru.job4j.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.model.Role;
import ru.job4j.model.User;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

@WebServlet(urlPatterns = {"/reg.do"})
public class RegServlet extends HttpServlet {
	
	private final HibernateRepository repository = new HibernateRepository(new HibernateRepositorySettings());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("jsp/reg.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			req.setAttribute("error", "Пустой email или пароль");
			req.getRequestDispatcher("jsp/reg.jsp").forward(req, resp);
		} else {
			User user = User.of(name, email, password, repository.findById(Role.class, "1"));
			User findUser = this.repository.findByEmail(User.class, email);
			if (findUser != null && findUser.getPassword().equals(password)) {
				req.setAttribute("error", "Повторяющийся email и пароль");
				req.getRequestDispatcher("jsp/reg.jsp").forward(req, resp);
			} else {
				this.repository.add(user);
				req.getRequestDispatcher("jsp/login.html").forward(req, resp);
			}
		}
	}
}
