package com.agibilibus.siget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.agibilibus.siget.controller.Controller;
import com.agibilibus.siget.dao.ReunionDAO;
import com.agibilibus.siget.model.Reunion;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestVisualizarCalendario {
	
	/*
	@Autowired
	private HttpSession sesion = new HttpSession();
	*/
	private static MockHttpSession sesion = new MockHttpSession();
	@Autowired
	private ReunionDAO reuniondao;
	private Controller controller = new Controller();
	
	@Test
	public void testGetReuniones() throws JSONException {
		
		
		Map<String, Object> credenciales = new HashMap<String, Object>();
		credenciales.put("userName", "Elisa");
		credenciales.put("pwd", "Seguridad2020");

		try {
			controller.login(sesion, credenciales);
		} catch (Exception e) {
			fail("Excepcion inesperada haciendo login en testGetReuniones: " + e);
		}
		JSONArray reuniones = new JSONArray(controller.getReuniones(sesion));
		Assert.assertTrue(reuniones.length() > 0);
	}
	
	@Test
	public void testUsuarioSinReuniones() throws JSONException {
	
		Map<String, Object> credenciales = new HashMap<String, Object>();
		credenciales.put("userName", "Elisa");
		credenciales.put("pwd", "Seguridad2020");

		try {
			controller.login(sesion, credenciales);
		} catch (Exception e) {
			fail("Excepcion inesperada haciendo login en testUsuarioSinReuniones: " + e);
		}
		JSONArray reuniones = new JSONArray(controller.getReuniones(sesion));
		Assert.assertEquals(0, reuniones.length());
	}
	
	/*
	@Test
	public void testVisualizarDatosReunion () throws JSONException, Exception {
		
		Map<String, Object> credenciales = new HashMap<String, Object>();
		Map<String, Object> loadReunion = new HashMap<String, Object>();
		Reunion reunion = reuniondao.findById("Elisa#graduacion#2020-12-01T10:00:00.000+01:00#2020-12-01T12:00:00.000+01:00#jaime").get();
		credenciales.put("userName", "Elisa");
		credenciales.put("pwd", "Seguridad2020");
		loadReunion.put("id", reunion.getIdReunion());

		try {
			controller.login(sesion, credenciales);
		} catch (Exception e) {
			fail("Excepcion inesperada haciendo login en testVisualizarDatosReunion: " + e);
		}
		JSONObject reunionjso = new JSONObject(controller.loadReunion(sesion, loadReunion));
		Assert.assertEquals(reunionjso.getString("id"), reunion.toJSON().getString("id"));

	}
	*/
	
}
	

	