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

    @Inject
    private static WSClient wsClient;

    private static Promise<JsonNode> callJsonApi(String url) {
        Promise<JsonNode> jsonPromise = wsClient.url(url).get().map(
                new Function<WSResponse, JsonNode>() {
                    public JsonNode apply(WSResponse wsResponse) {
                        JsonNode json = wsResponse.asJson();
                        return json;
                    }
                }
        );

        return jsonPromise;
    }

    public static Result search(String query) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

            // Call API
            Promise<JsonNode> jsonPromise = callJsonApi(url);
            JsonNode jsonNode = jsonPromise.get(10000);
            return ok(jsonNode);
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
}
