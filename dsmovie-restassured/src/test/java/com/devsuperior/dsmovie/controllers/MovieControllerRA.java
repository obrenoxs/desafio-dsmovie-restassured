package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

	private Map<String, Object> postMovieInstance;

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

		postMovieInstance = new HashMap<>();
		postMovieInstance.put("title", "Test Movie Title");
		postMovieInstance.put("score", 0.0);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://img.com/foo.jpg");
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

		given()
				.get("/movies/{id}", existingId)
				.then()
				.statusCode(200)
				.body("id", is(1))
				.body("title", equalTo("The Witcher"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {

		given()
				.get("/movies/{id}", nonExistingId)
				.then()
				.statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		postMovieInstance.put("title", "");
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("Authorization","Bearer " + adminToken)
				.body(newMovie.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("Authorization","Bearer " + clientToken)
				.body(newMovie.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("Authorization","Bearer " + invalidToken)
				.body(newMovie.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(401);
	}
}
