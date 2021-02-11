package ru.job4j.handler;

import java.io.PrintWriter;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;

import ru.job4j.model.Item;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

public class GetItemHandler {
	private final HibernateRepository repository;
	private final Jsonb jsonb;
	
	public GetItemHandler() {
		HibernateRepositorySettings settings = new HibernateRepositorySettings();
		this.repository = new HibernateRepository(settings);
		jsonb = JsonbBuilder.create();
	}
	
	public void findAll(PrintWriter w, String action, HttpServletRequest req) {
		List<Item> items = this.repository.findAll(Item.class);
		String result = jsonb.toJson(items);
		w.println(result);
		return;
	}
	
	public void findById(PrintWriter w, String action, HttpServletRequest req) {
		Item item = this.repository.findById(Item.class, action.substring(1));
		w.println(jsonb.toJson(item));
		return;
	}
	
	public void setDone(PrintWriter w, String action, HttpServletRequest req) {
		Item item = this.repository.findById(Item.class, action.substring(9));
		item.setDone(true);
		this.repository.replace(String.valueOf(item.getId()), item);
		return;
	}
	
	public void setNotDone(PrintWriter w, String action, HttpServletRequest req) {
		Item item = this.repository.findById(Item.class, action.substring(12));
		item.setDone(false);
		this.repository.replace(String.valueOf(item.getId()), item);
	}
}
