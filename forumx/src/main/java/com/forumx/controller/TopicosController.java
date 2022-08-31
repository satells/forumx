package com.forumx.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {

		if (nomeCurso == null) {
			Page<Topico> page = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(page);
		}

		Page<Topico> page = topicoRepository.findByCursoNomeStartingWithIgnoreCase(nomeCurso, paginacao);
		return TopicoDto.converter(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);

		if (topico.isPresent()) {

			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroDeFormulario("Código " + id, "Não encontrado"));
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		UriComponentsBuilder path = uriBuilder.path("/topicos/{id}");
		URI uri = path.buildAndExpand(topico.getId()).toUri();

		BodyBuilder bodyBuilder = ResponseEntity.created(uri);

		return bodyBuilder.body(new TopicoDto(topico));
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
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
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {

		Optional<Topico> topicoOptional = topicoRepository.findById(id);

		if (topicoOptional.isPresent()) {

			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(new ErroDeFormulario("Código " + id, "Não encontrado"));

	}

}