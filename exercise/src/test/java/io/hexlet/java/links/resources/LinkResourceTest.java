package io.hexlet.java.links.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import io.hexlet.java.links.RestApplication;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LinkResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = RestApplication.startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(RestApplication.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    @Test
    public void testUrlCreation() {
        final String url = "https://www.amazon.com/Vela-Laboratory-Reinforced-Wear" 
            + "-Resistant-Heavyweight/dp/B01LZDX5L6/ref=s9_wish_gw_g229_i2_r?_enco" 
            + "ding=UTF8&colid=2OXXWX6KZOIGT&coliid=IMV6HIP2CFWDO&fpl=fresh&pf_rd_" 
            + "m=ATVPDKIKX0DER&pf_rd_s=&pf_rd_r=3Z5Y7NB3BE2TJ9KJCFPZ&pf_rd_t=36701" 
            + "&pf_rd_p=1cded295-23b4-40b1-8da6-7c1c9eb81d33&pf_rd_i=desktop";
        final String id = target
                .path("links")
                .request()
                .put(Entity.entity(url, MediaType.TEXT_PLAIN))
                .readEntity(String.class);
        assertNotNull("id from the server should not be null", id);
        assertThat("id from the server should not be empty", id.isEmpty(), is(false));
        assertThat(
            String.format(
                "id of the url (%s) should not be equals to the fullUrl", 
                id, 
                url),
            id, 
            is(not(url)));
        final String resultUrl = target
                .path(String.format("links/%s", id))
                .request()
                .get(String.class);
        assertThat(
            String.format(
                "The resultUrl (%s) is not equals to the original url (%s).", 
                resultUrl, 
                url), 
            url, 
            is(resultUrl));
    }
}
