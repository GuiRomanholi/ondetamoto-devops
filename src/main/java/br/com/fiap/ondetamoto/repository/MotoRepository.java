package br.com.fiap.ondetamoto.repository;

import br.com.fiap.ondetamoto.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
}
