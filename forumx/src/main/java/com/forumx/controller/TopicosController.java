package com.forumx.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.transaction.annotation.Transactional;
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
		return TopicoDto.converter(topicoRepository.findByCursoNomeStartingWithIgnoreCase(nomeCurso));
	}

	@PostMapping
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

		Topico topico = topicoRepository.getTopicoById(id);

		if (topico == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroDeFormulario("Código " + id, "Não encontrado"));

		}
		return ResponseEntity.ok(new DetalhesDoTopicoDto(topico));

	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Topico topico = form.atualizar(id, topicoRepository);

		return ResponseEntity.ok(new TopicoDto(topico));
	}

}