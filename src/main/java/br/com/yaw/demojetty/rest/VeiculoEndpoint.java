package br.com.yaw.demojetty.rest;

import java.util.List;

import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.yaw.demojetty.model.Multa;
import br.com.yaw.demojetty.model.Veiculo;
import br.com.yaw.demojetty.repository.MultaRepository;
import br.com.yaw.demojetty.repository.VeiculoRepository;

/**
 * Rest EndPoint exposes operations to <code>Veiculo</code> entity.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
@Component
@Path("/veiculos")
public class VeiculoEndpoint {

	@Autowired
	private VeiculoRepository repository;
	
	@Autowired
	private MultaRepository multaRepository;
	
	@POST
	@Consumes({"application/json", "application/xml"})
	public Response create(Veiculo entity) {
		if (repository.exists(entity.getPlaca())) {
			return Response.status(Status.CONFLICT).build();
		}
		Veiculo v = repository.save(entity);
		return Response.created(UriBuilder.fromResource(VeiculoEndpoint.class).path(String.valueOf(v.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		if (!repository.exists(id)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		repository.delete(id);
		return Response.ok().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces({"application/json", "application/xml"})
	public Response findById(@PathParam("id") Long id) {
		try {
			Veiculo entity = repository.findOne(id);
			return Response.ok(entity).build();
		} catch (NoResultException nrex) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/{placa}")
	@Produces({"application/json", "application/xml"})
	public Response findByPlaca(@PathParam("placa") String placa) {
		Veiculo entity = repository.findByPlaca(placa);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}
	
	@GET
	@Produces({"application/json", "application/xml"})
	@Wrapped(element="veiculos", namespace="http://yaw.com.br", prefix="yaw")
	public List<Veiculo> listAll(
			@QueryParam("marca") @DefaultValue("") String marca,
			@QueryParam("from") @DefaultValue("0") int from,
			@QueryParam("to") @DefaultValue("20") int to) {
		if (from < 0) {
			from = 0;
		}
		if (to < 1) {
			to = 1;
		}
		
		Pageable pageable = new PageRequest(from, to);
		Page<Veiculo> dados;
		if ("".equals(marca)) {
			dados = repository.findAll(pageable);
		} else {
			dados = repository.findByMarca(marca, pageable);
		}
		return dados.getContent();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes({"application/json", "application/xml"})
	public Response update(@PathParam("id") Long id, Veiculo entity) {
		repository.save(entity);
		return Response.noContent().build();
	}
	
	@GET
	@Path("/{placa}/multas")
	@Produces({"application/json", "application/xml"})
	public Response listMultasByPlaca(@PathParam("placa") String placa) {
		List<Multa> multas = multaRepository.findByPlaca(placa);
		if (multas.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		GenericEntity<List<Multa>> entity = new GenericEntity<List<Multa>>(multas){};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/{veiculo:[0-9][0-9]*}/multas")
	@Produces({"application/json", "application/xml"})
	@Wrapped(element="multas", namespace="http://yaw.com.br", prefix="yaw")
	public List<Multa> listMultasByVeiculo(
			@PathParam("veiculo") Long idVeiculo) {
		Veiculo v = new Veiculo();
		v.setId(idVeiculo);
		
		List<Multa> multas = multaRepository.findByVeiculo(v);
		return multas;
	}

}