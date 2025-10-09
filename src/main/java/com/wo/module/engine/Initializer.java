package com.wo.module.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright @2019 Whiteopen
 */
/**
 * @version 1.0
 * @author
 */
public class Initializer extends HttpServlet {

	private static final long serialVersionUID = 8517709874076765683L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	public void init() throws ServletException {
		super.init();
		System.out.println("Initializer Oscar Engine Run");
//		AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");
		
		OscarEngine oscarEngine = new OscarEngine();
		oscarEngine.start();
	}
}