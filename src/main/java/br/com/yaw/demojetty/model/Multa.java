package br.com.yaw.demojetty.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity represents a traffict penalty (fine).
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
@XmlRootElement
@Entity
@Table(name="multa")
public class Multa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private String descricao;
	
	@NotNull
	private String tipo;
	
	@NotNull
	@Min(1l)
	private Integer pontos;
	
	private Double valor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataOcorrencia;
	
	/**
	 * 0 ou n penaltys to one vehicle.
	 */
	@NotNull
	@ManyToOne
	private Veiculo veiculo;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getPontos() {
		return pontos;
	}

	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}
	
	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}
	
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
