package ru.job4j.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.Role;
import ru.job4j.model.User;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/itemRestRepository/*")
public class ItemCrudServlet extends HttpServlet {
	private static String URI = "todolist/itemRestRepository";
	private static String URL_EMPTY = "";
	private static String URL_WITH_ID = "/\\d+";
	private static String URL_SET_DONE = "/setDone/\\d+";
	private static String URL_SET_NOTDONE = "/setNotDone/\\d+";

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
				List<Item> items = this.repository.findAll(Item.class);
				String result = jsonb.toJson(items);
				w.println(result);
			} else if (action.matches(URL_WITH_ID)) {
				Item item = this.repository.findById(Item.class, action.substring(1));
				w.println(jsonb.toJson(item));
			} else if (action.matches(URL_SET_DONE)) {
				Item item = this.repository.findById(Item.class, action.substring(9));
				item.setDone(true);
				this.repository.replace(String.valueOf(item.getId()), item);
			} else if (action.matches(URL_SET_NOTDONE)) {
				Item item = this.repository.findById(Item.class, action.substring(12));
				item.setDone(false);
				this.repository.replace(String.valueOf(item.getId()), item);
			} else {
				w.println("fail");
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
				String requestBody = req.getReader().readLine();
				System.out.println("(1)  " + requestBody);
				Item item = jsonb.fromJson(requestBody, Item.class);
				System.out.println("(2" + item);
				List<Category> categories = new ArrayList<Category>();
				List<Category> reqCategories = item.getCategories();
				for (Category category : reqCategories) {
					categories.add(repository.findById(Category.class, category.getId().toString()));
				}
				try {
					Item savedItem = this.repository.add(Item.of(item.getDescription(),
							User.of(item.getUser().getId(), repository.findById(Role.class, "1")), categories));
					w.println(jsonb.toJson(savedItem));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (action.matches(URL_WITH_ID)) {
				this.repository.delete(Item.class, action.substring(1));
				w.println("Delete with Id = " + action.substring(1));
			} else {
				w.println("fail");
			}
		}
	}
}
