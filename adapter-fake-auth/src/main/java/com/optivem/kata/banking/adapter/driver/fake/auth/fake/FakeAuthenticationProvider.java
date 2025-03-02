package com.optivem.kata.banking.adapter.driver.fake.auth.fake;

import com.optivem.kata.banking.adapter.driven.base.ProfileNames;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Profile(ProfileNames.ADAPTER_AUTH_FAKE)
public class FakeAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        var name = authentication.getName();
        var password = authentication.getCredentials().toString();
        var authorities = new ArrayList<GrantedAuthority>();
        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
