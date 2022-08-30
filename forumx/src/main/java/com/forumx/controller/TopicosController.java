package com.forumx.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.forumx.config.validacao.ErroDeFormulario;
import com.forumx.controller.dto.DetalhesDoTopicoDto;
import com.forumx.controller.dto.TopicoDto;
import com.forumx.controller.form.AtualizacaoTopicoForm;
import com.forumx.controller.form.TopicoForm;
import com.forumx.modelo.Topico;
import com.forumx.repository.CursoRepository;
import com.forumx.repository.TopicoRepository;

@RestController
@RequestMapping(path = "/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		if (nomeCurso == null) {
			return TopicoDto.converter(topicoRepository.findAll());
		}

		List<Topico> lista = topicoRepository.findByCursoNomeStartingWithIgnoreCase(nomeCurso);

		for (Topico topico : lista) {
			System.out.println(topico.getTitulo());
		}

		return TopicoDto.converter(lista);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		UriComponentsBuilder path = uriBuilder.path("/topicos/{id}");
		URI uri = path.buildAndExpand(topico.getId()).toUri();

		BodyBuilder bodyBuilder = ResponseEntity.created(uri);

		return bodyBuilder.body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);

		if (topico.isPresent()) {

			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroDeFormulario("Código " + id, "Não encontrado"));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> topicoOptional = topicoRepository.findById(id);
		if (topicoOptional.isPresent()) {

			Topico topico = form.atualizar(id, topicoRepository);

			return ResponseEntity.ok(new TopicoDto(topico));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(new ErroDeFormulario("Código " + id, "Não encontrado"));

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {

		Optional<Topico> topicoOptional = topicoRepository.findById(id);

		if (topicoOptional.isPresent()) {

			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(new ErroDeFormulario("Código " + id, "Não encontrado"));

	}

}