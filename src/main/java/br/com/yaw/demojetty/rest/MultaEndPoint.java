package br.com.yaw.demojetty.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import br.com.yaw.demojetty.model.Multa;
import br.com.yaw.demojetty.repository.MultaRepository;
import br.com.yaw.demojetty.repository.VeiculoRepository;

/**
 * Rest service exposes operations to <code>Multa</code> entity.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
@Component
@Path("/veiculos/multas")
public class MultaEndPoint {
	
	@Autowired
	private MultaRepository repository;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	private static final SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	@POST
	@Consumes({"application/json", "application/xml"})
	public Response create(Multa multa) {
		if (!veiculoRepository.exists(multa.getVeiculo().getId())) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Multa m = repository.save(multa);
		return Response.created(UriBuilder.fromResource(MultaEndPoint.class).path(String.valueOf(m.getId())).build()).build();
	}
	
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes({"application/json", "application/xml"})
	public Response update(@PathParam("id") Long id, Multa multa) {
		if (!veiculoRepository.exists(multa.getVeiculo().getId())) {
			return Response.status(Status.NOT_FOUND).build();
		}
		repository.save(multa);
		return Response.noContent().build();
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
	@Produces({"application/json", "application'/xml"})
	public Response findMarcaById(@PathParam("id") Long id) {
		try {
			Multa multa = repository.findOne(id);
			return Response.ok(multa).build();
		} catch (NoResultException nrex) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces({"application/json", "application/xml"})
	public Response listMultasByData(
			@QueryParam("data") String data,
			@QueryParam("from") @DefaultValue("0") int from,
			@QueryParam("to") @DefaultValue("20") int to) {
		if (data == null) {
			return Response.ok().build();
		}
		
		if (from < 0) {
			from = 0;
		}
		if (to < 1) {
			to = 1;
		}
		
		Date dataFiltro;
		try {
			dataFiltro = ddMMyyyyFormat.parse(data);
		} catch (ParseException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataFiltro);
		cal.add(Calendar.DATE, 1);
		
		List<Multa> multas = repository.findByDataOcorrencia(dataFiltro, cal.getTime(), new PageRequest(from, to));
		GenericEntity<List<Multa>> entity = new GenericEntity<List<Multa>>(multas){};
		return Response.ok(entity).build();
	}
	
}
