package com.nttdata.apirestdebitcard.service;

import com.nttdata.apirestdebitcard.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class APIClientsImpl implements APIClients {

    @Autowired
    private WebClient webClient;

    @Value("${config.base.enpoint.accounts}")
    private String urlAccounts;


    @Override
    public Flux<AccountDto> findAccountsByCustomer(String idCustomer) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", idCustomer);
        return webClient.get().uri(urlAccounts + "/customer/{id}", params).accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(response -> response.bodyToFlux(AccountDto.class));
    }

    @Override
    public Mono<AccountDto> findAccountByCustomer(String idAccount) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", idAccount);
        return webClient.get().uri(urlAccounts + "/{id}", params).accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> response.bodyToMono(AccountDto.class));
    }

    @Override
    public Mono<AccountDto> updateAccount(AccountDto accountDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", accountDto.getId());
        return webClient.put()
                .uri(urlAccounts + "/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountDto), AccountDto.class)
                .retrieve()
                .bodyToMono(AccountDto.class);
    }

    @Override
    public Mono<AccountDto> saveAccount(AccountDto accountDto) {
        Map<String, Object> params = new HashMap<String, Object>();
        System.out.println("accountDto.getId():" + accountDto.getId());
        params.put("id", accountDto.getId());
        return webClient.put().uri(urlAccounts + "/{id}", params)
                .body(Mono.just(accountDto), AccountDto.class)
                .retrieve()
                .bodyToMono(AccountDto.class);
    }
}
