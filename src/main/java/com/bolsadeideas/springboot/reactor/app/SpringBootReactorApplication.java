package com.bolsadeideas.springboot.reactor.app;

import com.bolsadeideas.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// ejemploIterable();
		// ejemploFlatMap();
		// ejemploToString();
		ejemploToCollectList();
	}



	public void ejemploToCollectList() {

		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Andres", "Guzman"));
		usuariosList.add(new Usuario("Pedro", "Ramirez"));
		usuariosList.add(new Usuario("Camilo" ,"Vargas"));
		usuariosList.add(new Usuario("Juan" ,"Gomez"));
		usuariosList.add(new Usuario("Cristian", "Torres"));
		usuariosList.add(new Usuario("Nelson" ,"Torres"));

		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(lista -> {
					lista.forEach(item -> log.info(item.toString()));
				});


	}


	public void ejemploToString() {

		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Andres", "Guzman"));
		usuariosList.add(new Usuario("Pedro", "Ramirez"));
		usuariosList.add(new Usuario("Camilo" ,"Vargas"));
		usuariosList.add(new Usuario("Juan" ,"Gomez"));
		usuariosList.add(new Usuario("Cristian", "Torres"));
		usuariosList.add(new Usuario("Nelson" ,"Torres"));

		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase() ))
				.flatMap(nombre -> {
					if( nombre.contains("andres".toUpperCase())) {
						return Mono.just(nombre);
					} else {
						return  Mono.empty();
					}
				})

				.map(String::toLowerCase).subscribe(log::info
				);

	}


	public void ejemploFlatMap()  {

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Ramirez");
		usuariosList.add("Camilo Vargas");
		usuariosList.add("Juan Gomez");
		usuariosList.add("Cristian Torres");
		usuariosList.add("Nelson Torres");

		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if( usuario.getNombre().equalsIgnoreCase("andres")) {
						return Mono.just(usuario);
					} else {
						return  Mono.empty();
					}
				})

				.map( usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				}).subscribe(e-> log.info(e.toString())
		);

	}


	public void ejemploIterable() {

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Ramirez");
		usuariosList.add("Camilo Vargas");
		usuariosList.add("Juan Gomez");
		usuariosList.add("Cristian Torres");
		usuariosList.add("Nelson Torres");


		Flux<String> nombres = Flux.fromIterable(usuariosList);


		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("andres") )
				.doOnNext( usuario -> {
					if( usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));

				})
				.map( usuario -> {
					String nombre = usuario.getNombre().toUpperCase();
					usuario.setNombre(nombre);
					return usuario;
				});


		usuarios.subscribe(e-> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Finish !!!");
					}
				}
		);

	}

}
