package com.optivem.kata.banking.adapter.driven.microservice.fake;

import com.optivem.kata.banking.core.ports.driven.CustomerGateway;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FakeCustomerGateway implements CustomerGateway {

    private static final boolean BLACKLISTED = true;
    private static final boolean WHITELISTED = false;

    private final Map<String, Boolean> customers;

    public FakeCustomerGateway() {
        this.customers = new HashMap<>();
    }

    @Override
    public boolean isBlacklisted(String nationalIdentityNumber) {
        if(!customers.containsKey(nationalIdentityNumber)) {
            return false;
        }
        var status = customers.get(nationalIdentityNumber);
        return isBlacklisted(status);
    }

    public void setupBlacklisted(String nationalIdentityNumber) {
        customers.put(nationalIdentityNumber, BLACKLISTED);
    }

    public void setupWhitelisted(String nationalIdentityNumber) {
        customers.put(nationalIdentityNumber, WHITELISTED);
    }

    private boolean isBlacklisted(boolean status) {
        return status == BLACKLISTED;
    }
}
