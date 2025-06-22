package com.projeto.repository;

import com.projeto.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByDisponivelTrue();
    Optional<Veiculo> findByIdAndDisponivelTrue(Long id);
}
