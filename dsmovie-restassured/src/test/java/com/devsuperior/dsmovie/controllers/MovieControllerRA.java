package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

	private String adminUsername;
	private String adminPassword;
	private String clientUsername;
	private String clientPassword;

	private String adminToken;
	private String clientToken;
	private String invalidToken;

	private Long existingId;
	private Long nonExistingId;

	private String movieTitle;

	@BeforeEach
	public void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		existingId = 1L;
		nonExistingId = 100L;

		movieTitle = "O Espetacular Homem-Aranha 2";

		clientUsername = "alex@gmail.com";
		adminUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto"; // Invalid Token
	}

	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

		given()
				.accept(ContentType.JSON)
				.when()
				.get("/movies")
				.then()
				.statusCode(200);
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

		given()
				.accept(ContentType.JSON)
				.when()
				.queryParam("title", movieTitle)
				.get("/movies")
				.then()
				.statusCode(200)
				.body("content.title[0]", containsString(movieTitle));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
