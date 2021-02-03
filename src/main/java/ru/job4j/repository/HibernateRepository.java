package ru.job4j.repository;

import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
			configuration.addAnnotatedClass(Item.class);
			registry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
		} catch (Exception e) {
		}
		sf = configuration.buildSessionFactory(registry);
	}
	
	private <T> T tx(final Function<Session, T> command) {
	    final Session session = this.sf.openSession();
	    final Transaction tx = session.beginTransaction();
	        T rsl = command.apply(session);
	        tx.commit();
	        session.close();
	        return rsl;
	}

	public Item add(Item item) {
		return (Item) this.tx(session -> session.save(item));
	}

	public boolean replace(String id, Item item) {
		return this.tx(session -> {
			Item res = session.get(Item.class, Integer.valueOf(id));
			res.setDescription(item.getDescription());
			res.setCreated(item.getCreated());
			res.setDone(item.isDone());
			session.update(res);
			return true;
		});
	}

	public boolean delete(String id) {
		return this.tx(session -> {
			Item item = Item.of(null, null, null);
			item.setId(Integer.valueOf(id));
			session.delete(item);
			return true;
		});
	}

	public List<Item> findAll() {
		return this.tx(session -> session.createQuery("SELECT i FROM Item i").getResultList());
	}

	public List<Item> findByName(String key) {
		return this.tx(session -> session.createQuery("SELECT i FROM Item i WHERE i.name = :name").setParameter("name", key).getResultList());
	}

	public Item findById(String id) {
		return this.tx(session -> session.get(Item.class, Integer.valueOf(id)));
	}
}
