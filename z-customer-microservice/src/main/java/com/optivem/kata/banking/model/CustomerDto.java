package com.optivem.kata.banking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    private String id;
    private boolean blacklisted;
}
