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

// Simula um banco SQL (Postgres/MySQL): por enquanto a lista faz o papel da tabela
// e a sequence começa em 1000, então os ids gerados são 1001, 1002, ...
@Service("produtoModelSql")
@Profile("sql")
public class ProdutoModelSql implements ProdutoModel {

    private final List<Produto> tabela = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1000);

    public ProdutoModelSql() {
        System.out.println("ProdutoModel em uso: ProdutoModelSql (perfil sql)");
    }

    @Override
    public Produto criar(ProdutoCreateRequest req) {
        Long novoId = sequence.incrementAndGet();
        Produto p = ProdutoMapper.toEntity(req, novoId);
        tabela.add(p);
        return p;
    }

    @Override
    public Optional<Produto> obterPorId(Long id) {
        return tabela.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
    }

    @Override
    public List<Produto> buscar(String nome, BigDecimal min, BigDecimal max) {
        return tabela.stream()
                .filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> min == null || p.getValor().compareTo(min) >= 0)
                .filter(p -> max == null || p.getValor().compareTo(max) <= 0)
                .toList();
    }
}
