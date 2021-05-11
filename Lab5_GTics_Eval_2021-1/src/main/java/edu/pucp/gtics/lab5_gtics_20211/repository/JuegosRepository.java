package edu.pucp.gtics.lab5_gtics_20211.repository;

import edu.pucp.gtics.lab5_gtics_20211.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20211.entity.JuegosUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface JuegosRepository extends JpaRepository<Juegos, Integer> {
    /** Completar */
    @Query(value = "select j.nombre, j.descripcion, u.idusuario, j.image from juegos j , usuarios u, juegosxusuario ju where j.idjuego=ju.idjuego and u.idusuario=ju.idusuario and u.idusuario=?1;", nativeQuery = true)
    List<JuegosUserDto> obtenerJuegosPorUser(int idusuario);
}
