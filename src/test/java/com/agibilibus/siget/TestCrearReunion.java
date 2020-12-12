package com.agibilibus.siget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.agibilibus.siget.controller.Controller;
import com.agibilibus.siget.dao.InvitacionDAO;
import com.agibilibus.siget.dao.ReunionDAO;
import com.agibilibus.siget.dao.UserDAO;
import com.agibilibus.siget.model.Invitacion;
import com.agibilibus.siget.model.Reunion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCrearReunion {

	private Controller controller = new Controller();
	private Map<String, Object> reunion = new HashMap<String, Object>();
	private MockHttpSession session = new MockHttpSession();
	private Reunion r = null;
	
	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private ReunionDAO reuniondao;
	
	@Autowired 
	private InvitacionDAO invitaciondao;
	
	@Before
	public void init() {
		session.setAttribute("user", userdao.findById("carlos").get());
		reunion.put("nombre", "Mañanera");
		reunion.put("fecha", "2020-10-01");
		reunion.put("horaInicio", "11:00:00");
		reunion.put("horaFin", "13:00:00");
		reunion.put("descripcion", "Fallos");
		reunion.put("url", "https://www.google.com/");
		reunion.put("correos", "a@gmail.com");
	}
	
	@After
	public void end() {
		reuniondao.deleteById(r.getIdReunion());
	}

	@Test
	public void testCrearReunion() throws Exception {
		r = controller.guardarReunion(session, reunion);
		Assert.assertNotNull(reuniondao.findById(r.getIdReunion()));
	}
	
	@Test(expected = org.springframework.dao.DuplicateKeyException.class)
	public void testCrearReunionYaExiste() throws Exception {
		r = controller.guardarReunion(session, reunion);
		controller.guardarReunion(session, reunion);
	}
	
	@Test
	public void creaInvitaciones() throws Exception {
		
		Map<String, Object> usuarioInvitado = new HashMap<String, Object>();
		usuarioInvitado.put("userCompletName", "usuario");
		usuarioInvitado.put("userName", "userparainvitar");
		usuarioInvitado.put("userApellidos", "parainvitar");
		usuarioInvitado.put("userDate", "2020-11-30");
		usuarioInvitado.put("userDni", "0000001");
		usuarioInvitado.put("userTelf", "666666666");
		usuarioInvitado.put("userMail", "invitado@gmail");
		usuarioInvitado.put("pwd1", "Hola1234");
		usuarioInvitado.put("id", "userparainvitar");
		controller.register(session, usuarioInvitado);
		
		reunion.put("correos","invitado@gmail");
		r = controller.guardarReunion(session, reunion);
		List<Invitacion> listaInvitaciones = invitaciondao.findAll();
		Invitacion invitacionCreada = null;
		for(Invitacion i : listaInvitaciones)
			if(i.getUsuario().getUser().equals("userparainvitar")&&i.getReunion().getIdReunion().equals(r.getIdReunion()))
				invitacionCreada = i;
		Assert.assertNotNull(invitacionCreada);
		invitaciondao.deleteById(invitacionCreada.getIdInvitacion());
		
	}

}
