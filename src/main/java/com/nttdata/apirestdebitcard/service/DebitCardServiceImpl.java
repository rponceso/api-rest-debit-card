/**
 * Implementation Interface Service CreditCard
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestdebitcard.service;

import com.nttdata.apirestdebitcard.dto.AccountDto;
import com.nttdata.apirestdebitcard.dto.FilterDto;
import com.nttdata.apirestdebitcard.model.DebitCard;
import com.nttdata.apirestdebitcard.repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private DebitCardRepository repository;

    @Autowired
    private APIClients apiClients;

    @Override
    public Mono<DebitCard> create(DebitCard debitCard) {

        Flux<AccountDto> fxAccounts = apiClients.findAccountsByCustomer(debitCard.getCustomer().getId());
        Mono<DebitCard> monoDebitCard = Mono.just(debitCard);

        return monoDebitCard
                .flatMap(dc -> apiClients.findAccountByCustomer(dc.getAccounts().get(0).getId())
                        .map(acc1 -> {
                            acc1.setPrincipal(true);
                            return acc1;
                        }).flatMap(acc2 -> apiClients.updateAccount(acc2))
                        .flatMap(acu -> fxAccounts.collectList().flatMap(lst -> {
                            debitCard.setAccounts(lst);
                            return repository.save(debitCard);
                        })));

    }

    @Override
    public Mono<DebitCard> update(DebitCard debitCard, String id) {
        Mono<DebitCard> monoBody = Mono.just(debitCard);
        Mono<DebitCard> monoBD = repository.findById(debitCard.getId());

        return monoBD
                .zipWith(monoBody, (bd, a) -> {
                    bd.setId(id);
                    bd.setAccounts(debitCard.getAccounts());
                    bd.setCardBrand(debitCard.getCardBrand());
                    bd.setCardType(debitCard.getCardType());
                    bd.setCustomer(debitCard.getCustomer());
                    bd.setActive(debitCard.isActive());
                    bd.setCreationDate(debitCard.getCreationDate());
                    bd.setCvv(debitCard.getCvv());
                    bd.setMonthYearExp(debitCard.getMonthYearExp());
                    bd.setNameCard(debitCard.getNameCard());
                    bd.setPan(debitCard.getPan());
                    return bd;
                })
                .flatMap(dc -> repository.save(dc));
    }

    @Override
    public Flux<DebitCard> listAll() {
        return repository.findAll();
    }

    @Override
    public Mono<DebitCard> getById(String id) {
        return repository.findById(id).flatMap(debitCard -> {
            Comparator<AccountDto> comparator = Comparator.comparing(acc -> acc.isPrincipal());
            comparator = comparator.thenComparing(Comparator.comparing(acc -> acc.getCreationDate()));
            List<AccountDto> listOrder = debitCard.getAccounts().stream().
                    sorted(comparator.reversed()).collect(Collectors.toList());
            debitCard.setAccounts(listOrder);
            return Mono.just(debitCard);
        });
    }

    @Override
    public Mono<DebitCard> getByPan(String pan) {
        return repository.findByPan(pan);
    }

    @Override
    public Mono<DebitCard> getByCustomer_Id(String customerId) {
        return repository.findByCustomer_Id(customerId);
    }

    @Override
    public Mono<DebitCard> getDebitCardWithAccountPrincipal(String idDebitCard) {
        Mono<DebitCard> mono = repository.findById(idDebitCard);
        return mono.flatMap(debitCard -> {
            List<AccountDto> lista = debitCard.getAccounts();
            List<AccountDto> listaFiltro = lista.stream().filter(element -> element.isPrincipal()).collect(Collectors.toList());
            debitCard.setAccounts(listaFiltro);
            return Mono.just(debitCard);
        });

    }

    @Override
    public Flux<DebitCard> findByCreationDateBetween(FilterDto filter) {
        return repository.findByCreationDateBetween(filter.getStartDate(), filter.getEndDate());
    }
}
