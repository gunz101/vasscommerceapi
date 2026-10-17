# CHECKLIST.md — VassCommerce (IoC/DI, Controllers x Models)

Estratégia de seleção usada: **@Profile** (`mem` e `sql`).

- (x) Controladores **não** fazem `new` de implementações; dependem de **interfaces**.
  O `ProdutoController` só conhece a interface `ProdutoModel` (`private final ProdutoModel model;`).
- (x) Há ao menos **duas** implementações `@Service` de `ProdutoModel`.
  `ProdutoModelMemoria` (`@Service("produtoModelMemoria")`) e `ProdutoModelSql` (`@Service("produtoModelSql")`).
- (x) A seleção é feita por `@Primary` **ou** `@Qualifier` **ou** `@Profile`.
  `@Profile("mem")` na `ProdutoModelMemoria` e `@Profile("sql")` na `ProdutoModelSql`.
  O perfil ativo é definido em `application.properties` (`spring.profiles.active=mem`).
- (x) Injeção é **por construtor**; nenhum `@Autowired` em campo.
  `public ProdutoController(ProdutoModel model)`. Como só existe um construtor, o Spring injeta sem `@Autowired`.
- (x) `ProdutoController` continua retornando **201/200/404** corretos.
  `POST /produto` → 201 + `Location: /produto/{id}`; `GET /produto/{id}` → 200 ou 404; `GET /produto` → 200.
- (x) Validação em `ProdutoCreateRequest` continua ativa (`@Valid`).
  `nome` com `@NotBlank`/`@Size`, `valor` com `@NotNull`/`@Positive`.
- (x) JSON de erro 400 continua **padronizado** via `@RestControllerAdvice`.
  `GlobalExceptionHandler` sempre devolve `{"error": "...", "fields": [{"name": "...", "message": "..."}]}`.

## Prova da troca de implementação

| Perfil | Implementação injetada | Ids gerados | Mensagem no console |
|---|---|---|---|
| `mem` (padrão) | `ProdutoModelMemoria` | 1, 2, 3... | `ProdutoModel em uso: ProdutoModelMemoria (perfil mem)` |
| `sql` | `ProdutoModelSql` | 1001, 1002... | `ProdutoModel em uso: ProdutoModelSql (perfil sql)` |

Para trocar, mude `spring.profiles.active` para `sql` no `application.properties`. O controller não muda.

Capturas: `postman/print-perfil-mem.png` e `postman/print-perfil-sql.png` mostram a aplicação subindo em cada
perfil e a implementação que o Spring injetou. As requisições 201/200/404/400 estão na coleção
`postman/VassCommerce-IoC-DI.postman_collection.json`.

## Critérios de avaliação

| Critério | Onde está |
|---|---|
| Desacoplamento | `controller/ProdutoController.java` depende só de `model/ProdutoModel.java` (interface) |
| IoC/DI | `@Service` + `@Profile` em `ProdutoModelMemoria` e `ProdutoModelSql`; injeção pelo construtor do controller |
| Contrato HTTP | 201 + `Location`, 200, 404; `produces="application/json"` na classe e `consumes="application/json"` no POST |
| Validação e erros | `@Valid` no `create`; `exception/GlobalExceptionHandler.java` (`@RestControllerAdvice`) padroniza o 400 |
| Qualidade (teste) | `ProdutoControllerTest`: implementação injetada, 201 + Location, 200, 404 e 400 |
