package ru.job4j.function;

@FunctionalInterface
public interface TriConsumer <T, U, R> {
	public void accept(T t, U u, R r);
}
