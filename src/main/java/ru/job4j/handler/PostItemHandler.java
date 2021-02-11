package ru.job4j.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;

import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.Role;
import ru.job4j.model.User;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

public class PostItemHandler {
	private final HibernateRepository repository;
	private final Jsonb jsonb;
	
	public PostItemHandler() {
		HibernateRepositorySettings settings = new HibernateRepositorySettings();
		this.repository = new HibernateRepository(settings);
		jsonb = JsonbBuilder.create();
	}
	
	public void save(PrintWriter w, String action, HttpServletRequest req) {
		String requestBody = null;
		try {
			requestBody = req.getReader().readLine();
			System.out.println("(1) " + requestBody);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Item item = jsonb.fromJson(requestBody, Item.class);
		List<Category> categories = new ArrayList<Category>();
		List<Category> reqCategories = item.getCategories();
		for (Category category : reqCategories) {
			categories.add(repository.findById(Category.class, category.getId().toString()));
		}
		try {
			Item savedItem = this.repository.add(Item.of(item.getDescription(),
					User.of(item.getUser().getId(), repository.findById(Role.class, "1")), categories));
//			w.println(jsonb.toJson(savedItem));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(PrintWriter w, String action, HttpServletRequest req) {
		this.repository.delete(Item.class, action.substring(1));
		w.println("Delete with Id = " + action.substring(1));
	}
	
}
