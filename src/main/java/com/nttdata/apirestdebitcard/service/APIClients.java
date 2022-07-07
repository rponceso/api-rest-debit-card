package com.nttdata.apirestdebitcard.service;


import com.nttdata.apirestdebitcard.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface APIClients {
    Flux<AccountDto> findAccountsByCustomer(String idCustomer);
    Mono<AccountDto> findAccountByCustomer(String idAccount);
    Mono<AccountDto> updateAccount(AccountDto accountDto);
    Mono<AccountDto> saveAccount(AccountDto accountDto);

}
