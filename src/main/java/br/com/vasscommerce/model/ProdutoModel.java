package br.com.vasscommerce.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import br.com.vasscommerce.dto.ProdutoCreateRequest;
import br.com.vasscommerce.entity.Produto;

public interface ProdutoModel {

    Produto criar(ProdutoCreateRequest req);       // regra de criação

    Optional<Produto> obterPorId(Long id);         // consulta por id

    List<Produto> buscar(String nome,
                         BigDecimal valorMinimo,
                         BigDecimal valorMaximo);  // filtros opcionais
}
