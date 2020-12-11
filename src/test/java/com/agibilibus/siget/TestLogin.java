package com.agibilibus.siget;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.agibilibus.siget.controller.Controller;
import com.agibilibus.siget.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLogin {
	
	private static Controller controller = new Controller();
	private static Map<String, Object> usuarioLogin = new HashMap<String, Object>();
	private static MockHttpSession session = new MockHttpSession();
	private Map<String, Object> credenciales = new HashMap<String, Object>();
	
	
	@Before
	public void init() throws NoSuchAlgorithmException, JSONException {
		usuarioLogin.put("userCompletName", "usuario");
		usuarioLogin.put("userName", "usuarioLogin");
		usuarioLogin.put("userApellidos", "login");
		usuarioLogin.put("userDate", "2020-11-30");
		usuarioLogin.put("userDni", "0000001");
		usuarioLogin.put("userTelf", "666666666");
		usuarioLogin.put("userMail", "user@login");
		usuarioLogin.put("pwd1", "Hola1234");
		usuarioLogin.put("id", "usuarioLogin");
		controller.register(session, usuarioLogin);
	}
	
	@After
	public void liberarRecursos() {
		Usuario.get().eliminarUsuario("usuarioLogin");
	}
	
	@Test
	public void testLoginCorrecto() throws Exception {
		credenciales.put("userName", "usuarioLogin");
		credenciales.put("pwd", "Hola1234");
		Assert.assertTrue(controller.login(session, credenciales));
	}
	
	@Test(expected = Exception.class)
	public void testUsuarioIncorrecto() throws Exception {	
		credenciales.put("userName", "usuarioLogi");
		credenciales.put("pwd", "Hola1234");
		controller.login(session, credenciales);
	}
	@Test(expected = Exception.class)
	public void testContraseñaIncorrecta() throws Exception {
		credenciales.put("userName", "usuarioLogin");
		credenciales.put("pwd", "Hola123");
		controller.login(session, credenciales);
	}
	

}
