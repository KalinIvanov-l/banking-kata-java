package com.optivem.kata.banking.adapter.driver.restapi.spring.controllers.base;

import an.awesome.pipelinr.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    protected Pipeline pipeline;

    @Autowired
    public BaseController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
}
