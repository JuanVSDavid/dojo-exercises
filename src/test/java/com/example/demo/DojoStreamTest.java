package com.example.demo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DojoStreamTest {

    List<Player> list;

    @BeforeEach
    void setUp(){
        list = CsvUtilFile.getPlayers();
    }

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();
         list.parallelStream().filter(player -> player.age >= 35)
                .flatMap(player1 -> list.parallelStream()
                        .filter(player2 -> player1.club.equals(player2.club))
                ).distinct()
                .collect(Collectors.groupingBy(Player::getClub));
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        var mejorJugadorConNacionalidadFrancia = list
                .stream()
                .filter(player -> "France".equals(player.getNational()))
                .max(Comparator.comparingDouble(o -> o.getWinners() / o.getGames()));
        System.out.println(mejorJugadorConNacionalidadFrancia);
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        var clubsAgrupadosPorNacionalidad = list
                .stream()
                .collect(Collectors.groupingBy(Player::getNational, Collectors.mapping(Player::getClub, Collectors.toSet())));
        System.out.println(clubsAgrupadosPorNacionalidad);
    }

    @Test
    void clubConElMejorJugador(){
        var clubConElMejorJugador = list.stream().max(Comparator.comparingDouble(o -> o.getWinners() / o.getGames())).get().getClub();
        System.out.println(clubConElMejorJugador);
    }


    @Test
    void mejorJugadorSegunNacionalidad(){
        var mejorJugadorSegunNacionalidad = list.stream().collect(Collectors.groupingBy(Player::getNational, Collectors.maxBy(Comparator.comparingDouble(o -> o.getWinners() / o.getGames()))));
        System.out.println(mejorJugadorSegunNacionalidad);
    }


}
