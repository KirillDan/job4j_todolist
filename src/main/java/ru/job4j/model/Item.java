package ru.job4j.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Item {
	@Id
	@GeneratedValue
	private Integer id;
	private String description;
	private Timestamp created;
	private Boolean done;
	
	@OneToOne
	@JoinColumn(name = "author")
	private User user;

	public static Item of(String description, Timestamp created, Boolean done) {
		Item item = new Item();
		item.description = description;
		item.created = created;
		item.done = done;
        return item;
    }
	
	public static Item of(String description) {
		Item item = new Item();
		item.description = description;
		item.created = Timestamp.valueOf(LocalDateTime.now());
		item.done = false;
		item.user = null;
        return item;
    }
	
	public static Item of(String description, User user) {
		Item item = new Item();
		item.description = description;
		item.created = Timestamp.valueOf(LocalDateTime.now());
		item.done = false;
		item.user = user;
        return item;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
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

	@Override
	public String toString() {
		return "Item [id=" + id + ", description=" + description + ", created=" + created + ", done=" + done + "]";
	}
	
}
