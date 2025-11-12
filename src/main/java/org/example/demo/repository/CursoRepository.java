package org.example.demo.repository;

import org.example.demo.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    @Query("SELECT c FROM Curso c JOIN FETCH c.usuario")
    List<Curso> findAllWithUsuario();
}
