package br.com.vasscommerce.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.vasscommerce.model.ProdutoModel;
import br.com.vasscommerce.model.ProdutoModelMemoria;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mem")
class ProdutoControllerTest {

    private final MockMvc mockMvc;
    private final ProdutoModel model;

    @Autowired
    ProdutoControllerTest(MockMvc mockMvc, ProdutoModel model) {
        this.mockMvc = mockMvc;
        this.model = model;
    }

    @Test
    void perfilMemInjetaProdutoModelMemoria() {
        assertInstanceOf(ProdutoModelMemoria.class, model);
    }

    @Test
    void criarProdutoRetorna201ComLocation() throws Exception {
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Teclado\", \"valor\": 150.00}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Teclado"));
    }

    @Test
    void buscarProdutoExistenteRetorna200() throws Exception {
        String location = mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Mouse\", \"valor\": 80.00}"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mouse"));
    }

    @Test
    void buscarProdutoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/produto/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoInvalidoRetorna400Padronizado() throws Exception {
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\", \"valor\": -10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input"))
                .andExpect(jsonPath("$.fields", hasSize(2)));
    }
}
