package ru.job4j.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {
	@Id
	@GeneratedValue
	private Integer id;
	private String description;
	private Timestamp created;
	private Boolean done;
	
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
	public void setDone(boolean done) {
		this.done = done;
	}
	
}
