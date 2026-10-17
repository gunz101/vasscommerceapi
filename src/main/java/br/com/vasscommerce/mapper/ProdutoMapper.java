package br.com.vasscommerce.mapper;

import br.com.vasscommerce.dto.ProdutoCreateRequest;
import br.com.vasscommerce.dto.ProdutoResponse;
import br.com.vasscommerce.entity.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {}

    // Entrada da API -> Entidade
    public static Produto toEntity(ProdutoCreateRequest req, Long novoId) {
        Produto p = new Produto();
        p.setId(novoId);
        p.setNome(req.getNome());
        p.setDescricao(req.getDescricao());
        p.setValor(req.getValor());
        return p;
    }

    // Entidade -> Saída da API
    public static ProdutoResponse toResponse(Produto p) {
        ProdutoResponse r = new ProdutoResponse();
        r.setId(p.getId());
        r.setNome(p.getNome());
        r.setDescricao(p.getDescricao());
        r.setValor(p.getValor());
        return r;
    }
}
