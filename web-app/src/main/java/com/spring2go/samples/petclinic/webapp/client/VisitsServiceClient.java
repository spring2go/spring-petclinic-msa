/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spring2go.samples.petclinic.webapp.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import com.spring2go.samples.petclinic.webapp.dto.VisitDetails;
import com.spring2go.samples.petclinic.webapp.dto.Visits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * @author Maciej Szarlinski
 */
@Component
@RequiredArgsConstructor
public class VisitsServiceClient {

    @Value("${petclinic.visits-service-endpoint}")
    private String visitsServiceEndpoint;

    private final RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "emptyVisitsForPets")
    public Map<Integer, List<VisitDetails>> getVisitsForPets(final List<Integer> petIds) {
        UriComponentsBuilder builder = fromHttpUrl(visitsServiceEndpoint + "/pets/visits")
                .queryParam("petId", joinIds(petIds));

        return restTemplate.getForObject(builder.toUriString(), Visits.class)
                .getItems()
                .stream()
                .collect(groupingBy(VisitDetails::getPetId));
    }

    private String joinIds(List<Integer> petIds) {
        return petIds.stream().map(Object::toString).collect(joining(","));
    }

    private Map<Integer, List<VisitDetails>> emptyVisitsForPets(List<Integer> petIds) {
        return Collections.emptyMap();
    }
}
