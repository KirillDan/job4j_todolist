package ru.job4j.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

@WebServlet("/categoryRestRepository/*")
public class CategoryCrudservlet extends HttpServlet {
	private static String URI = "todolist/categoryRestRepository";
	private static String URL_EMPTY = "";
	
	private HibernateRepository repository;
	private Jsonb jsonb;
	
	public void init() {
		HibernateRepositorySettings settings = new HibernateRepositorySettings();
		this.repository = new HibernateRepository(settings);
		jsonb = JsonbBuilder.create();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		String startUri = "/" + URI;
		int firstIndex = uri.indexOf(startUri);
		if (firstIndex == 0) {
			String action = uri.substring(firstIndex + startUri.length());
			resp.setContentType("text/plain");
	        resp.setCharacterEncoding("UTF-8");
	        resp.setHeader("Access-Control-Allow-Origin", "*");
			PrintWriter w = resp.getWriter();
			if (action.matches(URL_EMPTY)) {
				List<Category> categories = this.repository.findAll(Category.class);
				String result = jsonb.toJson(categories);
				w.println(result);
			} else {
				w.println("fail");
			}
		}
	}
}
