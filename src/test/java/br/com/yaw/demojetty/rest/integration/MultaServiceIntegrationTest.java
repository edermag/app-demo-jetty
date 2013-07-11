package br.com.yaw.demojetty.rest.integration;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Before;
import org.junit.Test;

import br.com.yaw.demojetty.model.Multa;
import br.com.yaw.demojetty.model.Veiculo;

/**
 * Integration tests to <i>Multa</i> service.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
public class MultaServiceIntegrationTest {

	/**
	 * URL to Veiculo REST service.
	 */
	private static final String VEICULO_END_POINT = "http://localhost:8000/appdemojetty/api/veiculos";
	
	/**
	 * URL to Multa REST service.
	 */
	private static final String MULTA_END_POINT = "http://localhost:8000/appdemojetty/api/veiculos/multas";

	@Before
	public void setup() {
	}
	
	private Veiculo prepareVeiculo(String placa) throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT+"/"+placa);
		ClientResponse<String> response = request.get();
		
		Veiculo v;
		if (response.getStatus() == 200) {
			v = response.getEntity(Veiculo.class);
		} else {
			long hash = new Date().getTime();
			
			v = new Veiculo();
			v.setPlaca(placa);
			v.setChassi("CHSMLT"+hash);
			v.setRenavam("RNVMLT"+hash);
			v.setMarca("Honda");
			
			request = new ClientRequest(VEICULO_END_POINT);
			request.body("application/json", v);
			
			response = request.post(String.class);
			Assert.assertEquals(response.getStatus(), 201);
			
			request = new ClientRequest(VEICULO_END_POINT+"/"+placa);
			response = request.get();
			Assert.assertEquals(response.getStatus(), 200);
			
			v = response.getEntity(Veiculo.class);
		}
		
		return v;
	}
		
	@Test
	public void createMultaTest() throws Exception {
		String placa = "MLT-0001";
		Veiculo v = prepareVeiculo(placa);
		
		Multa m = new Multa();
		m.setDataOcorrencia(new Date());
		m.setTipo("Rodovia");
		m.setValor(100.0d);
		m.setDescricao("Multa teste");
		m.setPontos(5);
		m.setVeiculo(v);
		
		ClientRequest requestMulta = new ClientRequest(MULTA_END_POINT);
		requestMulta.accept("application/json");
		requestMulta.body("application/json", m);
		
		ClientResponse<String> responseMulta = requestMulta.post(String.class);
		Assert.assertEquals(responseMulta.getStatus(), 201);
	}
	
	@Test
	public void createAndFindMultaTest() throws Exception {
		String placa = "MLT-0002";
		Veiculo v = prepareVeiculo(placa);
		
		Multa m = new Multa();
		m.setDataOcorrencia(new Date());
		m.setTipo("Rodovia");
		m.setValor(100.0d);
		m.setDescricao("Multa teste 2");
		m.setPontos(5);
		m.setVeiculo(v);
		
		ClientRequest requestMulta = new ClientRequest(MULTA_END_POINT);
		requestMulta.accept("application/json");
		requestMulta.body("application/json", m);
		
		ClientResponse<String> responseMulta = requestMulta.post(String.class);
		Assert.assertEquals(responseMulta.getStatus(), 201);
		
		requestMulta = new ClientRequest(VEICULO_END_POINT+"/"+placa+"/multas");
		responseMulta = requestMulta.get();
		Assert.assertEquals(responseMulta.getStatus(), 200);
	}
	
	@Test
	public void updateMultaTest() throws Exception {
		String placa = "MLT-0003";
		Veiculo v = prepareVeiculo(placa);
		
		Multa m = new Multa();
		m.setDataOcorrencia(new Date());
		m.setTipo("Rodovia");
		m.setValor(100.0d);
		m.setDescricao("Multa teste 3");
		m.setPontos(5);
		m.setVeiculo(v);
		
		ClientRequest requestMulta = new ClientRequest(MULTA_END_POINT);
		requestMulta.accept("application/json");
		requestMulta.body("application/json", m);
		
		ClientResponse<String> responseMulta = requestMulta.post(String.class);
		Assert.assertEquals(responseMulta.getStatus(), 201);
		
		requestMulta = new ClientRequest(VEICULO_END_POINT+"/"+placa+"/multas");
		responseMulta = requestMulta.get();
		Assert.assertEquals(responseMulta.getStatus(), 200);
		
		List<Multa> lista = (List<Multa>) responseMulta.getEntity(new GenericType<List<Multa>>(){});
		
		Multa consulta = lista.get(0);
		consulta.setDescricao("Modificado");
		consulta.setPontos(7);
		
		requestMulta = new ClientRequest(MULTA_END_POINT+"/"+consulta.getId());
		requestMulta.accept("application/json");
		requestMulta.body("application/json", consulta);
		
		responseMulta = requestMulta.put();
		
		Assert.assertEquals(responseMulta.getStatus(), 204);
	}
	
	@Test
	public void deleteMultaTest() throws Exception {
		String placa = "MLT-0004";
		Veiculo v = prepareVeiculo(placa);
		
		Multa m = new Multa();
		m.setDataOcorrencia(new Date());
		m.setTipo("Rodovia");
		m.setValor(100.0d);
		m.setDescricao("Multa teste 4");
		m.setPontos(5);
		m.setVeiculo(v);
		
		ClientRequest requestMulta = new ClientRequest(MULTA_END_POINT);
		requestMulta.accept("application/json");
		requestMulta.body("application/json", m);
		
		ClientResponse<String> responseMulta = requestMulta.post(String.class);
		Assert.assertEquals(responseMulta.getStatus(), 201);
		
		requestMulta = new ClientRequest(VEICULO_END_POINT+"/"+placa+"/multas");
		responseMulta = requestMulta.get();
		Assert.assertEquals(responseMulta.getStatus(), 200);
		
		List<Multa> lista = (List<Multa>) responseMulta.getEntity(new GenericType<List<Multa>>(){});
		
		Multa consulta = lista.get(0);
		consulta.setDescricao("Modificado");
		consulta.setPontos(7);
		
		requestMulta = new ClientRequest(MULTA_END_POINT+"/"+consulta.getId());
		responseMulta = requestMulta.delete();
		
		Assert.assertEquals(responseMulta.getStatus(), 200);
	}
	
	@Test
	public void listMultasByDataTest() throws Exception {
		String dataFiltro = "09-07-2013";
		ClientRequest request = new ClientRequest(MULTA_END_POINT + "?data=" + dataFiltro);
		ClientResponse<?> response = request.get();
		Assert.assertEquals(response.getStatus(), 200);
		
		List<Multa> lista = (List<Multa>) response.getEntity(new GenericType<List<Multa>>(){});
		System.out.println("Resultado get (full) \n");
		for (Object o: lista) {
			System.out.println("\t"+o);
		}
	}
	
}
