package br.com.vasscommerce.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.vasscommerce.dto.ProdutoCreateRequest;
import br.com.vasscommerce.entity.Produto;
import br.com.vasscommerce.mapper.ProdutoMapper;

@Service("produtoModelMemoria")
@Profile("mem")
public class ProdutoModelMemoria implements ProdutoModel {

    private final List<Produto> banco = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    public ProdutoModelMemoria() {
        System.out.println("ProdutoModel em uso: ProdutoModelMemoria (perfil mem)");
    }

    @Override
    public Produto criar(ProdutoCreateRequest req) {
        Long novoId = seq.incrementAndGet();
        Produto p = ProdutoMapper.toEntity(req, novoId);
        banco.add(p);
        return p;
    }

    @Override
    public Optional<Produto> obterPorId(Long id) {
        return banco.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
    }

    @Override
    public List<Produto> buscar(String nome, BigDecimal min, BigDecimal max) {
        return banco.stream()
                .filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> min == null || p.getValor().compareTo(min) >= 0)
                .filter(p -> max == null || p.getValor().compareTo(max) <= 0)
                .toList();
    }
}
