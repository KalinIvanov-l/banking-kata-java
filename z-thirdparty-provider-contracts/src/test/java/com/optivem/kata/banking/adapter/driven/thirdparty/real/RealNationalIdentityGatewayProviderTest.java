package com.optivem.kata.banking.adapter.driven.thirdparty.real;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpsTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;

@Provider("national-identity-provider")
@PactFolder("../adapter-thirdparty-real/build/pacts")
@SpringBootTest
class RealNationalIdentityGatewayProviderTest {

    @Value("${national.identity.provider.url}")
    private String path;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @SneakyThrows
    @BeforeEach
    void before(PactVerificationContext context) {
        var url = new URL(path);
        var testTarget = HttpsTestTarget.fromUrl(url);
        context.setTarget(testTarget);
    }

    @State("GET User: a user with the specified ID already exists")
    public void toGetUserExistsState() {
        // NOTE: Nothing can be done since we don't have control over the third party service
    }
}
