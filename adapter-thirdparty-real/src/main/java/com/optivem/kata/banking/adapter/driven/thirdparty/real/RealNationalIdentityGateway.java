package com.optivem.kata.banking.adapter.driven.thirdparty.real;

import com.optivem.kata.banking.adapter.driven.base.ProfileNames;
import com.optivem.kata.banking.core.ports.driven.NationalIdentityGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Profile(ProfileNames.ADAPTER_THIRDPARTY_REAL)
public class RealNationalIdentityGateway implements NationalIdentityGateway {
    private static final String PATH = "users/%s";

    private final String url;

    public RealNationalIdentityGateway(@Value("${national.identity.gateway.url}") String url) {
        this.url = url;
    }

    @Override
    public boolean exists(String nationalIdentityNumber) {
        var client = WebClient.create(url);
        var path = getPath(nationalIdentityNumber);
        var responseSpec = client.get().uri(path).retrieve();

        var user = responseSpec
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.empty())
                .bodyToMono(UserDto.class)
                .block();

        if(user == null) {
            return false;
        }
        var expectedName = "...";
        return user.getId() != null && expectedName.equals(user.getName());
    }

    private String getPath(String nationalIdentityNumber) {
        return String.format(PATH, nationalIdentityNumber);
    }
}
