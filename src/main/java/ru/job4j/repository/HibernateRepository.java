package ru.job4j.repository;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import ru.job4j.model.Item;

public class HibernateRepository {
	private SessionFactory sf;

	public HibernateRepository() {
		Configuration configuration = new Configuration();
		StandardServiceRegistry registry = null;
		try {
			Properties settings = new Properties();
			settings.put(Environment.DRIVER, "org.postgresql.Driver");
			settings.put(Environment.URL, "jdbc:postgresql://127.0.0.1:5432/tracker");
			settings.put(Environment.USER, "postgres");
			settings.put(Environment.PASS, "123");
			settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
			settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
			settings.put(Environment.HBM2DDL_AUTO, "update");
			settings.put(Environment.SHOW_SQL, "true");
			settings.put(Environment.FORMAT_SQL, "true");
			settings.put(Environment.USE_SQL_COMMENTS, "true");
			configuration.setProperties(settings);
			registry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
		} catch (Exception e) {
		}
		sf = configuration.buildSessionFactory(registry);
	}

	public Item add(Item item) {
		Session session = this.sf.openSession();
		session.beginTransaction();
		session.save(item);
		session.getTransaction().commit();
		session.close();
		return item;
	}

	public boolean replace(String id, Item item) {
		Session session = this.sf.openSession();
		session.beginTransaction();
		Item res = session.get(Item.class, Integer.valueOf(id));
		res.setDescription(item.getDescription());
		res.setCreated(item.getCreated());
		res.setDone(item.isDone());
		session.update(res);
		session.getTransaction().commit();
		session.close();
		return false;
	}

	public boolean delete(String id) {
		Session session = this.sf.openSession();
		session.beginTransaction();
		Item item = Item.of(null, null, null);
		item.setId(Integer.valueOf(id));
		session.delete(item);
		session.getTransaction().commit();
		session.close();
		return false;
	}

	public List<Item> findAll() {
		Session session = this.sf.openSession();
		session.beginTransaction();
		List<Item> result = session.createQuery("SELECT i FROM Item i").getResultList();
		session.getTransaction().commit();
		session.close();
		return result;
	}

	public List<Item> findByName(String key) {
		Session session = this.sf.openSession();
		session.beginTransaction();
		List<Item> result = session.createQuery("SELECT i FROM Item i WHERE i.name = :name").setParameter("name", key)
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return result;
	}

	public Item findById(String id) {
		Session session = this.sf.openSession();
		session.beginTransaction();
		Item result = session.get(Item.class, Integer.valueOf(id));
		session.getTransaction().commit();
		session.close();
		return result;
	}
}
