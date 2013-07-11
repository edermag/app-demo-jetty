package br.com.yaw.demojetty.rest.integration;

import java.util.Date;

import java.util.List;
import org.jboss.resteasy.util.GenericType;

import junit.framework.Assert;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;

import br.com.yaw.demojetty.model.Veiculo;

/**
 * Integration tests to <i>Veiculo</i> service.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
public class VeiculoServiceIntegrationTest {

	private static final String VEICULO_END_POINT = "http://localhost:8000/appdemojetty/api/veiculos";

	@Before
	public void setup() {
	}
	
	@Test
	public  void getVeiculosTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		ClientResponse<String> response = request.get(String.class);
		Assert.assertEquals(response.getStatus(), 200);
    }
	
	@Test
	public void createVeiculoTest() throws Exception {
		long hash = new Date().getTime();
		String[] marcas = {"GM", "Volks", "Honda"};
		int nroPlaca = (int) (Math.random()*9999)+1;
		String placa = "JNT-"+nroPlaca;

		Veiculo v = new Veiculo();
		v.setPlaca(placa);
		v.setChassi("CHS"+hash);
		v.setRenavam("RNV"+hash);
		v.setMarca(marcas[(int) Math.random()*3]);
		
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		request.accept("application/json");
		request.body("application/json", v);
		ClientResponse<String> response = request.post(String.class);
		Assert.assertEquals(response.getStatus(), 201);
		
		request = new ClientRequest(VEICULO_END_POINT+"/"+placa);
		response = request.get();
		Veiculo consulta = response.getEntity(Veiculo.class);
		System.out.println("Veiculo pesquisado apos post: "+consulta.getId());
	}
	
	@Test
	public void listVeiculosTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		ClientResponse<?> response = request.get();
		Assert.assertEquals(response.getStatus(), 200);
		
		List<Veiculo> lista = (List<Veiculo>) response.getEntity(new GenericType<List<Veiculo>>(){});
		System.out.println("Resultado get (full) \n");
		for (Object o: lista) {
			System.out.println("\t"+o);
		}
	}
	
	@Test
	public void getVeiculoByIdNotFoundTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT+"/"+-1);
		ClientResponse<?> response = request.get();
		Assert.assertEquals(response.getStatus(), 404);
	}
	
	@Test
	public void getVeiculoByPlacaNotFoundTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT+"/"+"XXX-0000");
		ClientResponse<?> response = request.get();
		Assert.assertEquals(response.getStatus(), 404);
	}
	
	@Test
	public void deleteVeiculoTest() throws Exception {
		long hash = new Date().getTime();
		String placa = "DEL-0001";

		Veiculo v = new Veiculo();
		v.setPlaca(placa);
		v.setChassi("CHS"+hash);
		v.setRenavam("RNV"+hash);
		v.setMarca("Volks");
		
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		request.accept("application/json");
		request.body("application/json", v);
		ClientResponse<String> response = request.post(String.class);
		Assert.assertEquals(response.getStatus(), 201);
		
		request = new ClientRequest(VEICULO_END_POINT+"/"+placa);
		response = request.get();
		Veiculo consulta = response.getEntity(Veiculo.class);
		
		long idDelete = consulta.getId();
		request = new ClientRequest(VEICULO_END_POINT+"/"+idDelete);
		response = request.delete(String.class);
		Assert.assertEquals(response.getStatus(), 200);
	}
	
	@Test
	public void createAndUpdateVeiculoTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		request.accept("application/json");
		
		int nroPlaca = (int) (Math.random()*9999)+1;
		String placa = "UPD-"+nroPlaca;
		Veiculo v = new Veiculo();
		v.setPlaca(placa);
		v.setChassi("UPDATE ");
		v.setRenavam("RNVUPDT");
		v.setMarca("Volks");
		
		request.body("application/json", v);
		ClientResponse<String> response = request.post(String.class);
		
		Assert.assertEquals(response.getStatus(), 201);
		
		request = new ClientRequest(VEICULO_END_POINT+"/"+placa);
		request.accept("application/json");
		response = request.get();
		
		Veiculo consulta = response.getEntity(Veiculo.class);
		consulta.setChassi("DEPOIS DO UPDATE");
		
		request = new ClientRequest(VEICULO_END_POINT+"/"+consulta.getId());
		request.body("application/json", consulta);
		
		response = request.put(String.class);
		Assert.assertEquals(response.getStatus(), 204);
		
	}
	
	@Test
	public void createVeiculoDuplicateTest() throws Exception {
		ClientRequest request = new ClientRequest(VEICULO_END_POINT);
		request.accept("application/json");
		
		String placa = "DUP-0001";
		Veiculo v = new Veiculo();
		v.setPlaca(placa);
		v.setChassi("UPDATE ");
		v.setRenavam("RNVUPDT");
		v.setMarca("Volks");
		
		request.body("application/json", v);
		ClientResponse<String> response = request.post(String.class);
		if (response.getStatus() == 201) {
			request = new ClientRequest(VEICULO_END_POINT);
			request.accept("application/json");
			request.body("application/json", v);
			response = request.post(String.class);
			Assert.assertEquals(response.getStatus(), 409); //CONFLICT
		}
	}

	@Test
	public void deleteVeiculoNofFoundTest() throws Exception {
		Long idDelete = 0l;
		ClientRequest request = new ClientRequest(VEICULO_END_POINT+"/"+idDelete);
		ClientResponse<String> response = request.delete(String.class);
		Assert.assertEquals(response.getStatus(), 403);
	}
}
