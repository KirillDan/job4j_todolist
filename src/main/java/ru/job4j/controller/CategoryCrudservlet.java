package ru.job4j.controller;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.handler.GetCategoryHandler;
import ru.job4j.router.Router;

@WebServlet("/categoryRestRepository/*")
public class CategoryCrudservlet extends GenericServlet {
	private final static String URI = "todolist/categoryRestRepository";
	private final static String URL_EMPTY = "";

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpServletResponse httpServletResponse = (HttpServletResponse) res;
		Router router = new Router(httpServletRequest, httpServletResponse, URI);
		GetCategoryHandler getHandler = new GetCategoryHandler();
		
		router.route(URL_EMPTY, "GET").handler(getHandler::findAll);
	}
}
