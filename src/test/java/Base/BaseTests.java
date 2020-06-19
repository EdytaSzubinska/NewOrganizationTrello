package Base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTests {

    protected final String BASE_URL = "https://api.trello.com/1/";
    protected final String BOARDS = "boards";
    protected final String LISTS = "lists";
    protected final String CARDS = "cards";
    protected final String ORGANIZATIONS = "organizations";

    protected static final String KEY = "KEY";
    protected static final String TOKEN = "TOKEN";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
    }
}

