package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F.*;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by Danny on 9/25/2014.
 */
public class WebService extends Controller {

    private static Promise<JsonNode> callJsonApi(String url) {
        return WS.url(url).get().map((WSResponse response) -> response.asJson());
    }

    public static Promise<Result> search(String query) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

            // Call API
            return callJsonApi(url).map((JsonNode jsonNode) -> ok(jsonNode));
        } catch (Exception e) {
            return Promise.promise(() -> internalServerError(e.getMessage()));
        }
    }
}
