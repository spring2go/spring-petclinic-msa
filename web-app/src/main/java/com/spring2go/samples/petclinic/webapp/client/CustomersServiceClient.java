package com.spring2go.samples.petclinic.webapp.client;

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

import lombok.RequiredArgsConstructor;
import com.spring2go.samples.petclinic.webapp.dto.OwnerDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Maciej Szarlinski
 */
@Component
@RequiredArgsConstructor
public class CustomersServiceClient {

    @Value("${petclinic.customers-service-endpoint}")
    private String customersServiceEndpoint;

    private final RestTemplate restTemplate;

    public OwnerDetails getOwner(final int ownerId) {
        return restTemplate.getForObject(customersServiceEndpoint + "/owners/{ownerId}", OwnerDetails.class, ownerId);
    }
}
