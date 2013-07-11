package br.com.yaw.demojetty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.yaw.demojetty.model.Veiculo;

/**
 * Define a contract for repository JPA (CRUD) to <code>Veiculo</code> entity.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

	/**
	 * Example shows a query with one named parameter.
	 * @param placa
	 * @return find a <code>Veiculo</code> by <code>placa</code>.
	 */
	@Query("select v from Veiculo v where v.placa = :placa")
	Veiculo findByPlaca(@Param("placa") String placa);
	
	/**
	 * Example show a simple query with pagination.
	 * @param marca
	 * @param pageable
	 * @return list of <code>Veiculo</code> by <code>marca</code>.
	 */
	@Query("select v from Veiculo v where v.marca = ?1")
	Page<Veiculo> findByMarca(String marca, Pageable pageable);
	
	/**
	 * @param placa
	 * @return Check if a <code>Veiculo</code> has already been registered with this <code>placa</code>.
	 */
	@Query("select case when count(v) > 0 then true else false end from Veiculo v where v.placa = ?1")
	boolean exists(String placa);

}
