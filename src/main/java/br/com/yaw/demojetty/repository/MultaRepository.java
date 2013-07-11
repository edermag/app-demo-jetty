package br.com.yaw.demojetty.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.yaw.demojetty.model.Multa;
import br.com.yaw.demojetty.model.Veiculo;

/**
 * Define a contract for repository JPA (CRUD) to <code>Multa</code> entity.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
public interface MultaRepository extends JpaRepository<Multa, Long> {

	/**
	 * Example shows a simple query with one parameter.
	 * @param veiculo
	 * @return list of <code>Multa</code> according to <code>Veiculo</code>.
	 */
	@Query("select m from Multa m where m.veiculo = ?1")
	List<Multa> findByVeiculo(Veiculo veiculo);
	
	/**
	 * Example shows use of named parameter.
	 * @param placa
	 * @return list of <code>Multa</code> by <code>Placa</code> of Vehicle.
	 */
	@Query("select m from Multa m where m.veiculo.placa = :placa")
	List<Multa> findByPlaca(@Param("placa") String placa);
	
	/**
	 * Example shows use of named parameter and pagination.
	 * @param dtInicio
	 * @param dtFim
	 * @param pageable
	 * @return list of <code>Multa</code> between initial and final <code>dataOcorrencia</code>.
	 */
	@Query("select m from Multa m where m.dataOcorrencia between :dataInicio and :dataFim")
	List<Multa> findByDataOcorrencia(@Param("dataInicio") Date dtInicio,
			@Param("dataFim") Date dtFim, Pageable pageable);
	
}