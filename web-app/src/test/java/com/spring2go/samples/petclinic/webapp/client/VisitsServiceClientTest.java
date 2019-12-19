package com.spring2go.samples.petclinic.webapp.client;

import com.spring2go.samples.petclinic.webapp.dto.VisitDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VisitsServiceClientTest {
    private static final Integer PET_ID = 1;

    @Autowired
    private VisitsServiceClient visitsServiceClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getVisitsForPets_withAvailableVisitsService() {
        mockServer.expect(requestTo("http://localhost:8083/pets/visits?petId=1"))
                .andRespond(withSuccess("{\"items\":[{\"id\":5,\"date\":\"2018-11-15\",\"description\":\"test visit\",\"petId\":1}]}", MediaType.APPLICATION_JSON));

        Map<Integer, List<VisitDetails>> visits = visitsServiceClient.getVisitsForPets(Collections.singletonList(1));

        assertVisitDescriptionEquals(visits, PET_ID,"test visit");
    }

    /**
     * Test Hystrix fallback method
     */
    @Test
    public void getVisitsForPets_withServerError() {

        mockServer.expect(requestTo("http://localhost:8083/pets/visits?petId=1"))
                .andRespond(withServerError());

        Map<Integer, List<VisitDetails>> visits = visitsServiceClient.getVisitsForPets(Collections.singletonList(1));

        assertEquals(0, visits.size());
    }

    private void assertVisitDescriptionEquals(Map<Integer, List<VisitDetails>> visits, int petId, String description) {
        assertEquals(1, visits.size());
        assertNotNull(visits.get(1));
        assertEquals(1, visits.get(petId).size());
        assertEquals(description, visits.get(petId).get(0).getDescription());
    }
}
