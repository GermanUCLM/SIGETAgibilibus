package com.agibilibus.siget;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.agibilibus.siget.controller.Controller;
import com.agibilibus.siget.dao.ReunionDAO;
import com.agibilibus.siget.dao.UserDAO;
import com.agibilibus.siget.model.Reunion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCrearReunionFail {

	private Controller controller = new Controller();
	private Map<String, Object> reunion = new HashMap<String, Object>();
	private MockHttpSession session = new MockHttpSession();
	private Reunion r = null;
	
	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private ReunionDAO reuniondao;
	
	@Before
	public void init() {
		session.setAttribute("user", userdao.findById("carlos").get());
		reunion.put("nombre", "Fail");
		reunion.put("fecha", "");
		reunion.put("horaInicio", "11:00:00");
		reunion.put("horaFin", "13:00:00");
		reunion.put("descripcion", "NoDate");
		reunion.put("url", "https://www.google.com/");
		reunion.put("correos", "a@gmail.com");
	}

	@Test(expected = NumberFormatException.class)
	public void testCrearReunionFail() throws Exception {
		r = controller.guardarReunion(session, reunion);
		Assert.assertNotNull(reuniondao.findById(r.getIdReunion()));
	}

}
