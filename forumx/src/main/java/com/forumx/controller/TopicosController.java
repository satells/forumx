package com.forumx.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.forumx.controller.dto.DetalhesDoTopicoDto;
import com.forumx.controller.dto.TopicoDto;
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
	public List<TopicoDto> lista() {
//		List<Topico> topicos = topicoRepository.findByCursoNomeStartingWithIgnoreCase("Spring");
		List<Topico> topicos = topicoRepository.findAll();

		return TopicoDto.converter(topicos);
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

	@GetMapping(path = "/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
		System.out.println(id);

		Topico topico = topicoRepository.getTopicoById(id);

		return new DetalhesDoTopicoDto(topico);
	}

}