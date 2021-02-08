package ru.job4j.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Item {
	@Id
	@GeneratedValue
	private Integer id;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
    private Date created;
	private Boolean done;
	
	@OneToOne
	@JoinColumn(name = "author")
	private User user;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private List<Category> categories = new ArrayList<>();

	public static Item of(String description, Date created, Boolean done) {
		Item item = new Item();
		item.description = description;
		item.created = created;
		item.done = done;
        return item;
    }
	
	public static Item of(String description) {
		Item item = new Item();
		item.description = description;
		item.created = new Date(System.currentTimeMillis());
		item.done = false;
		item.user = null;
        return item;
    }
	
	public static Item of(String description, User user, List<Category> categories) {
		Item item = new Item();
		item.description = description;
		item.created = new Date(System.currentTimeMillis());
		item.done = false;
		item.user = user;
		item.categories = categories;
        return item;
    }

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(Boolean done) {
		this.done = done;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void addCategories(Category category) {
		this.categories.add(category);
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", description=" + description + ", created=" + created + ", done=" + done
				+ ", categories=" + categories + "]";
	}
}
