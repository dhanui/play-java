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
    private WSClient wsClient;

    private static JsonNode callJsonApi(String url, long timeout) {
        return WS.url(url).get().map(
                new Function<WSResponse, JsonNode>() {
                    public JsonNode apply(WSResponse wsResponse) {
                        JsonNode json = wsResponse.asJson();
                        return json;
                    }
                }
        ).get(timeout);
    }

    public static Result search(String query) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

            // Call API
            JsonNode jsonNode = callJsonApi(url, 10000);
            return ok(jsonNode);
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
}
