/**
 * Interface Service CreditCard
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestdebitcard.service;

import com.nttdata.apirestdebitcard.dto.FilterDto;
import com.nttdata.apirestdebitcard.model.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {

    Mono<DebitCard> create(DebitCard debitCard);

    Mono<DebitCard> update(DebitCard debitCard, String idDebitCard);

    Flux<DebitCard> listAll();

    Mono<DebitCard> getById(String id);

    Mono<DebitCard> getByPan(String pan);

    Mono<DebitCard> getByCustomer_Id(String customerId);

    Mono<DebitCard> getDebitCardWithAccountPrincipal(String idDebitCard);

    Flux<DebitCard> findByCreationDateBetween(FilterDto filter);
}
