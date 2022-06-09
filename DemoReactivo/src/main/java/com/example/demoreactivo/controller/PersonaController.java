package com.example.demoreactivo.controller;

import com.example.demoreactivo.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    private static final Logger log = LoggerFactory.getLogger(PersonaController.class);

    @GetMapping("/mostrar")
    public Mono<Persona> mostrar(){
        return Mono.just(new Persona(1,"Rafael"));
    }

    @GetMapping
    public Flux<Persona> listar(){
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1, "Rafael"));
        personaList.add(new Persona(2, "Carlos"));

        Flux<Persona> personaFlux = Flux.fromIterable(personaList);
        return personaFlux;
    }

    @GetMapping("/response")
    public Mono<ResponseEntity<Flux<Persona>>> listarEntity(){
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1, "Marcos"));
        personaList.add(new Persona(2, "Carlos"));

        Flux<Persona> personaFlux= Flux.fromIterable(personaList);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personaFlux));
    }

    @DeleteMapping("/{modo}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable("modo") Integer modo){
        return buscarPersona(modo)
                .flatMap(p ->{
                    return eliminar(p)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
                }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    public Mono<Void> eliminar(Persona p){
        log.info("Eliminado " + p.getIdPersona() + " - " + p.getNombre());
        return Mono.empty();
    }

    public Mono<Persona> buscarPersona(Integer modo){
        if (modo == 1){
            return Mono.just(new Persona(1, "Carlos"));
        }else{
            return Mono.empty();
        }
    }
}
