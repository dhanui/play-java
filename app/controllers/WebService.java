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

    private static Promise<JsonNode> callJsonApi(String url) {
        return WS.url(url).get().map(
                new Function<WSResponse, JsonNode>() {
                    public JsonNode apply(WSResponse wsResponse) {
                        JsonNode json = wsResponse.asJson();
                        return json;
                    }
                }
        );
    }

    public static Promise<Result> search(String query) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

            // Call API
            Promise<JsonNode> jsonNode = callJsonApi(url);
            return jsonNode.map(
                    new Function<JsonNode, Result>() {
                        @Override
                        public Result apply(JsonNode jsonNode) throws Throwable {
                            return ok(jsonNode);
                        }
                    }
            );
        } catch (Exception e) {
            return Promise.promise(
                    new Function0<Result>() {
                        @Override
                        public Result apply() throws Throwable {
                            return internalServerError();
                        }
                    }
            );
        }
    }
}
