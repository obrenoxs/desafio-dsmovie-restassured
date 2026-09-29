package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ScoreControllerRA {

	private String clientUsername;
	private String clientPassword;
	private String clientToken;

	private Long existingMovieId;
	private Long nonExistingMovieId;

	private Map<String, Object> putScoreInstance;

	@BeforeEach
	public void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		existingMovieId = 1L;
		nonExistingMovieId = 100L;

		clientUsername = "alex@gmail.com";
		clientPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);

		putScoreInstance = new HashMap<>();

		putScoreInstance.put("movieId", existingMovieId);
		putScoreInstance.put("score", 4.0);
	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		putScoreInstance.put("movieId", nonExistingMovieId);
		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Authorization","Bearer " + clientToken)
				.body(newScore.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putScoreInstance.remove("movieId");
		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Authorization","Bearer " + clientToken)
				.body(newScore.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		putScoreInstance.put("score", -1.0);
		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Authorization","Bearer " + clientToken)
				.body(newScore.toString())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
}
