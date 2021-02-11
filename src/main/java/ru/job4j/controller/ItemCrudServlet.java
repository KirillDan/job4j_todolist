package ru.job4j.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.handler.GetItemHandler;
import ru.job4j.handler.PostItemHandler;
import ru.job4j.router.Router;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/itemRestRepository/*")
public class ItemCrudServlet extends GenericServlet {
	private final static String URI = "todolist/itemRestRepository";
	private final static String URL_EMPTY = "";
	private final static String URL_WITH_ID = "/\\d+";
	private final static String URL_SET_DONE = "/setDone/\\d+";
	private final static String URL_SET_NOTDONE = "/setNotDone/\\d+";

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpServletResponse httpServletResponse = (HttpServletResponse) res;
		Router router = new Router(httpServletRequest, httpServletResponse, URI);
		GetItemHandler getHandler = new GetItemHandler();
		PostItemHandler postHandler = new PostItemHandler();
		
		router.route(URL_EMPTY, "GET").handler(getHandler::findAll);
		router.route(URL_WITH_ID, "GET").handler(getHandler::findById);
		router.route(URL_SET_DONE, "GET").handler(getHandler::setDone);
		router.route(URL_SET_NOTDONE, "GET").handler(getHandler::setNotDone);
		router.route(URL_EMPTY, "POST").handler(postHandler::save);
		router.route(URL_WITH_ID, "POST").handler(postHandler::delete);
	}
}
