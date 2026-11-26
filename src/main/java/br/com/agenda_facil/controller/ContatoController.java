package br.com.agenda_facil.controller;

// Importa as classes necessárias.
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.agenda_facil.model.Contato;
import br.com.agenda_facil.repository.ContatoRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

// Indica que esta classe é um Controller do Spring.
// @Controller informa ao Spring que esta classe controla
// as requisições e as páginas da aplicação.
@Controller
public class ContatoController {

    // Referência ao repositório responsável pelo acesso ao banco.
    // "final": depois de recebido no construtor, não muda mais.
    private final ContatoRepository contatoRepository;

    // Construtor: o Spring fornece automaticamente o repositório.
    // Esse mecanismo é chamado de injeção de dependência:
    // não usamos "new", o Spring cria e entrega o objeto pronto.
    public ContatoController(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @GetMapping("/contatos")
    public String listar(Model model) {

        model.addAttribute("contatos", contatoRepository.findAll());

        // Count: Envia a quantidade de contatos para a pagina
        model.addAttribute("total", contatoRepository.count());

        // Abre o arquivo templates/contatos.html
        return "contatos";
    }

    @GetMapping("/contatos/novo")
    public String novo(Model model) {

        // Envia um objeto vazio para o formulario preeencher
        model.addAttribute("contato", new Contato());

        // informa que estamos cadastrando um novo contato
        model.addAttribute("titulo", "novo contato");

        // Abre o arquivo templates/formulario.html
        return "formulario";
    }

    // ==========================================
    @PostMapping("/contatos/salvar")

    // @Valid: executa as validações do Model
    // @ModelAttribute monta um objeto Contato com os campos do formulario
    // BindingResult guarda o resultado da validaçõa(deve ir logo após o objeto
    // validado)
    public String salvar(@Valid @ModelAttribute("contato") Contato contato, BindingResult resultado, Model model) {

        // Verifica se os dados enviados passaram pelas validações
        if (resultado.hasErrors()) {

            // Mantém o título correto: sem ID é cadastro, com ID é edição
            model.addAttribute("titulo", contato.getId() == null ? "Novo contato" : "Editar contato");

            // Se houver erro, retorna ao formulário sem salvar
            // As mensagens de erro aparecem nos campos(tr:errors)
            return "formulario";
        }

        // Se o Id existir vazio, cadastra(INSERT)
        // Se o Id existir vazio, atualiza o registro(UPDATE)
        contatoRepository.save(contato);

        // Redireciona para a lista. Isso evita reenviar o formulario ao atualizar a
        // página(F5)
        return "redirect:/contatos";
    }

    @GetMapping("/contatos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Contato contato = contatoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Contato não encontrado: " + id));

        model.addAttribute("contato", contato);
        model.addAttribute("titulo", "Editar contato");

        return "formulario";
    }

    // Mapeia a rota para excluir um contato pelo ID
    @PostMapping("/contatos/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        contatoRepository.deleteById(id);

        // Retorna para o contato
        return "redirect:/contatos";
    }

}
