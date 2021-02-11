package ru.job4j.handler;

import java.io.PrintWriter;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;

import ru.job4j.model.Category;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

public class GetCategoryHandler {
	private final HibernateRepository repository;
	private final Jsonb jsonb;
	
	public GetCategoryHandler() {
		HibernateRepositorySettings settings = new HibernateRepositorySettings();
		this.repository = new HibernateRepository(settings);
		jsonb = JsonbBuilder.create();
	}
	
	public void findAll(PrintWriter w, String action, HttpServletRequest req) {
		List<Category> categories = this.repository.findAll(Category.class);
		String result = jsonb.toJson(categories);
		w.println(result);
		return;
	}
}
