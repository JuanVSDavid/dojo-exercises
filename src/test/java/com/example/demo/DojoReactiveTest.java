package com.example.demo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DojoReactiveTest {

    Flux<Player> list;

    @BeforeEach
    void setUp(){
        list = Flux.fromIterable(CsvUtilFile.getPlayers());
    }

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }



    @Test
    void jugadoresMayoresA35SegunClub(){
        var list = Flux.fromStream(CsvUtilFile.getPlayers().parallelStream()).cache();
        list.filter(player -> player.age >= 35)
                .buffer(10)
                .flatMap(player1 -> list.filter(player2 -> player1.parallelStream().anyMatch(a -> player2.club.equals(a.club))))
                        .distinct()
                        .collectMultimap(Player::getClub);

        list.collectList().block();
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        var mejorJugadorConNacionalidadFrancia = list.filter(player -> "France".equals(player.getNational())).collect(Collectors.maxBy(Comparator.comparingDouble(o -> o.getWinners() / o.getGames()))).subscribe(System.out::println);
        //System.out.println(mejorJugadorConNacionalidadFrancia);
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        var clubsAgrupadosPorNacionalidad = list.collect(Collectors.groupingBy(Player::getNational, Collectors.mapping(Player::getClub, Collectors.toSet()))).block();
        System.out.println(clubsAgrupadosPorNacionalidad);
    }

    @Test
    void clubConElMejorJugador(){
        var clubConElMejorJugador = list.collect(Collectors.maxBy(Comparator.comparingDouble(o -> o.getWinners() / o.getGames()))).block().get().getClub();
        System.out.println(clubConElMejorJugador);
    }


    @Test
    void mejorJugadorSegunNacionalidad(){
        var mejorJugadorSegunNacionalidad = list.collect(Collectors.groupingBy(Player::getNational, Collectors.maxBy(Comparator.comparingDouble(o -> o.getWinners() / o.getGames())))).block();
        System.out.println(mejorJugadorSegunNacionalidad);
    }



}
