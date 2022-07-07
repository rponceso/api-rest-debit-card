package com.nttdata.apirestdebitcard.repository;

import com.nttdata.apirestdebitcard.model.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {

    Mono<DebitCard> findByPan(String pan);

    Mono<DebitCard> findByCustomer_Id(String customerId);

    Flux<DebitCard> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);
}
