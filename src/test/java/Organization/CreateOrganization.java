package Organization;

import Base.BaseTests;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrganization extends BaseTests {

    private Faker faker;
    private String displayName;
    private String website;
    private String orgDisplayName;

    public CreateOrganization() {
    }

    @BeforeEach
    public void beforeEach() {
        faker = new Faker();
        displayName = faker.name().title();
        website = faker.internet().url();
        orgDisplayName = faker.name().name();
    }

    @Test
    public void createNewOrganizationWithName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        String organizationId = json.get("id");
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrganizationsWithEmptyDisplayName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        String errorMessage = "Display Name must be at least 1 character";
        JsonPath json = response.jsonPath();
        assertThat(json.getString("message")).isEqualTo(errorMessage);
    }

    @Test
    public void createOrganisationWithToShortName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("name", "zz")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK) //prawidłowo powinno być "statusCode 400", ale jest błąd w Trello i przepuszcza z nazwą poniżej 3 znaków
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        deleteResource(BOARDS, json.getString("id"));
    }

    @Test
    public void createOrganizationWithWebsite() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgDisplayName)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("website")).contains(website);

        deleteResource(BOARDS, json.getString("id"));
    }

    @Test
    public void createOrganisationWithWrongUrl() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("website", "htt://test.pl")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK) // powinien być "statusCode 400", niemniej jest błąd i Trello "poprawia" url
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        deleteResource(BOARDS, json.getString("id"));
    }
}

