package ru.job4j.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateRepository {
	private SessionFactory sf;

	public HibernateRepository(HibernateRepositorySettings settings) {
		this.sf = settings.getSessionFactory();
	}

	private <T> T tx(final Function<Session, T> command) {
		final Session session = sf.openSession();
	    final Transaction tx = session.beginTransaction();
	    try {
	        T rsl = command.apply(session);
	        tx.commit();
	        return rsl;
	    } catch (final Exception e) {
	        session.getTransaction().rollback();
	        return null;
	    } finally {
	        session.close();
	    }
	}

	public <T> T add(T model) {
		return (T) this.tx(session -> session.save(model));
	}

	public <T> boolean replace(String id, T model) {
		return this.tx(session -> {
			T res = (T) session.find(model.getClass(), Integer.valueOf(id));
			Method[] methods = model.getClass().getMethods();
			for (Method method : methods) {
				int startIndex = -1;
				if (!method.getName().equals("getId") && !method.getName().equals("getClass")) {
					if (method.getName().indexOf("get") == 0) {
						startIndex = 3;
					} else if (method.getName().indexOf("is") == 0) {
						startIndex = 2;
					}
					if (startIndex != -1) {
						try {
							System.out.println("--------------------------------------------------------");
							System.out.println("method.getName() = " + method.getName());
							Method resMethod;
							Field resField;
							resField = res.getClass().getDeclaredField(method.getName().substring(startIndex).toLowerCase());
							resMethod = res.getClass().getMethod("set" + method.getName().substring(startIndex),
									resField.getType());
							System.out.println(resMethod);
							System.out.println("--------------------------------------------------------");
							resMethod.invoke(res, method.invoke(model, null));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
							e.printStackTrace();
						}
					}
				}
			}
			session.update(res);
			return true;
		});

	}

	public <T> boolean delete(Class<T> cl, String id) {
		return this.tx(session -> {
			session.createQuery("DELETE FROM " + cl.getName() + " u WHERE u.id = " + id);
			return true;
		});
	}

	public <T> List<T> findAll(Class<T> cl) {
		return this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u").getResultList());
	}

	public <T> T findById(Class<T> cl, String id) {
		return this.tx(session -> session.get(cl, Integer.valueOf(id)));
	}

	public <T> List<T> findByName(Class<T> cl, String key) {
		return this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u WHERE u.name = :name")
				.setParameter("name", key).getResultList());
	}

	public <T> T findByEmail(Class<T> cl, String key) {
		return (T) this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u WHERE u.email = :email")
				.setParameter("email", key).getSingleResult());
	}
}
