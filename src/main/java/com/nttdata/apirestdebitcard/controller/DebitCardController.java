/**
 * Controller that receives the requests
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestdebitcard.controller;

import com.nttdata.apirestdebitcard.dto.FilterDto;
import com.nttdata.apirestdebitcard.model.DebitCard;
import com.nttdata.apirestdebitcard.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/debit-card")
public class DebitCardController {

    @Autowired
    private DebitCardService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<DebitCard>>> list() {
        Flux<DebitCard> fxCreditCards = service.listAll();

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCreditCards));
    }

    @GetMapping("/{pan}")
    public Mono<ResponseEntity<DebitCard>> getByPan(@PathVariable("pan") String pan) {
        return service.getByPan(pan)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p)
                ); //Mono<ResponseEntity<CreditCard>>
    }

    @PostMapping
    public Mono<ResponseEntity<DebitCard>> register(@RequestBody DebitCard debitCard, final ServerHttpRequest req) {
        return service.create(debitCard)
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DebitCard>> update(@PathVariable("id") String id, @RequestBody DebitCard debitCard) {
        return service.update(debitCard, id).map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<DebitCard>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/customer/{customerId}")
    public Mono<ResponseEntity<Mono<DebitCard>>> getByCustomer_Id(@PathVariable("customerId") String customerId) {
        Mono<DebitCard> monoDebitCard = service.getByCustomer_Id(customerId);

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(monoDebitCard));
    }

    @GetMapping("/{idDebitCard}/principal")
    public Mono<ResponseEntity<Mono<DebitCard>>> getDebitCardWithAccountPrincipal(@PathVariable("idDebitCard") String idDebitCard) {
        Mono<DebitCard> monoDebitCard = service.getDebitCardWithAccountPrincipal(idDebitCard);

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(monoDebitCard));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<DebitCard>>> getDebitCard(@PathVariable("id") String idDebitCard) {
        Mono<DebitCard> monoDebitCard = service.getById(idDebitCard);
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(monoDebitCard));
    }

    @PostMapping("/reporting")
    public Mono<ResponseEntity<Flux<DebitCard>>> reporting(@RequestBody FilterDto filter) {
        Flux<DebitCard> fxDebitCards = service.findByCreationDateBetween(filter);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxDebitCards)
        );

    }


}
