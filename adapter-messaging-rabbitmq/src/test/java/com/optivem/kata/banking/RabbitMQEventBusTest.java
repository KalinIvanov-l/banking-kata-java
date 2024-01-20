package com.optivem.kata.banking;

import com.optivem.kata.banking.adapter.driven.base.ProfileNames;
import com.optivem.kata.banking.core.ports.driven.events.AccountOpenedDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;


@Profile(ProfileNames.ADAPTER_MESSAGING_RABBITMQ)
@Slf4j
class RabbitMQEventBusTest {
    @Mock
    private RabbitTemplate rabbitTemplate;
    private RabbitMQEventBus rabbitMQEventBus;
    private final String exchangeName = "test-exchange";
    private final String routingKeyName = "test-routingkey";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        rabbitMQEventBus = new RabbitMQEventBus(rabbitTemplate, exchangeName, routingKeyName);
    }

    @Test
    void shouldSendEventToCorrectExchangeAndRoutingKey() {
        var accountOpenedDto = new AccountOpenedDto(LocalDateTime.now(), 12345L, "John", "Doe", 1);
        rabbitMQEventBus.publish(accountOpenedDto);

        verify(rabbitTemplate).convertAndSend(
                ArgumentMatchers.eq(exchangeName),
                ArgumentMatchers.eq(routingKeyName),
                ArgumentMatchers.any(AccountOpenedDto.class),
                ArgumentMatchers.any(MessagePostProcessor.class)
        );
    }

    private static Stream<Arguments> should_return_AccountOpenedDto_event() {
        final var accountId = 35535L;
        return Stream.of(Arguments.of(new AccountOpenedDto(LocalDateTime.now(),accountId,"Sam","Brook",1)),
                Arguments.of(new AccountOpenedDto(LocalDateTime.now(),accountId,"Josh","Long",1)));
    }

    @ParameterizedTest
    @MethodSource("should_return_AccountOpenedDto_event")
    void should_send_dto_to_default_queue(AccountOpenedDto accountOpenedDto){
        rabbitMQEventBus.publish(accountOpenedDto);
    }

    @Test
    void should_consume_message_from_queue(){
            var message = rabbitTemplate.receiveAndConvert("default-bankingkata-queue");
            if (message != null) {
                if(message instanceof AccountOpenedDto accountOpenedDto){
                    log.info("{}", accountOpenedDto);
                }
            } else {
                log.error("No messages in the queue.");
            }
    }
}