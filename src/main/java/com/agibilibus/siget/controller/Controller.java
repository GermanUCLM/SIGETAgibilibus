package com.agibilibus.siget.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.agibilibus.siget.dao.UserDAO;
import com.agibilibus.siget.model.Invitacion;
import com.agibilibus.siget.model.Reunion;
import com.agibilibus.siget.model.Sesion;
import com.agibilibus.siget.model.Usuario;

@RestController
public class Controller {
	private static String error = "error";
	private static String message = "message";
	private static final String ZONA_HORARIA = "Europe/Madrid";
	private static final String USER_NAME = "userName";
	private static final String CORREOS = "correos";
	

	@Autowired
	private UserDAO userdao;

	@GetMapping("/")
	public ModelAndView inicio(ModelMap model) {
		return new ModelAndView("redirect:/Login.html", model);
	}

	@PostMapping("/login")
	public boolean login(HttpSession session, @RequestBody Map<String, Object> credenciales) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			JSONObject jso = new JSONObject(credenciales);
			String userName = jso.getString(USER_NAME);
			byte[] datosDesencriptados = md.digest(jso.getString("pwd").getBytes(StandardCharsets.UTF_8));
			String pwd = new String(datosDesencriptados);
			return Sesion.get().login(session, userName, pwd);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@PostMapping("/register")
	public String register(HttpSession session, @RequestBody Map<String, Object> credenciales)
	        throws NoSuchAlgorithmException, JSONException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		JSONObject jso = new JSONObject(credenciales);
		String userCompletName = jso.getString("userCompletName");
		String userName = jso.getString(USER_NAME);
		String userApellidos = jso.getString("userApellidos");
		String userDate = jso.getString("userDate");
		String userDni = jso.getString("userDni");
		int userTelf = Integer.parseInt(jso.getString("userTelf"));
		String userMail = jso.getString("userMail");
		byte[] datosDesencriptados = md.digest(jso.getString("pwd1").getBytes(StandardCharsets.UTF_8));
		String pwd1 = new String(datosDesencriptados);

		DateTime fecha = DateTime.parse(userDate);

		JSONObject resultado = new JSONObject();

		try {
			Usuario.get().crearUsuario(pwd1, userCompletName, userName, userApellidos, fecha, userDni, userTelf,
			        userMail);
			resultado.put("type", "OK");

		} catch (Exception e) {
			resultado.put("type", error);
			resultado.put(message, e.getMessage());
		}

		return resultado.toString();
	}

	@PostMapping("/nuevaTarea")
	public Reunion guardarReunion(HttpSession session, @RequestBody Map<String, Object> datosReunion) throws Exception {
		JSONObject jso = new JSONObject(datosReunion);
		String titulo = jso.getString("nombre");
		String descripcion = jso.getString("descripcion");
		String[] fecha = jso.getString("fecha").split("-");
		String[] horaIni = jso.getString("horaInicio").split(":");
		String[] horaFin = jso.getString("horaFin").split(":");
		DateTime horaI = new DateTime(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]),
				Integer.parseInt(fecha[2]), Integer.parseInt(horaIni[0]), Integer.parseInt(horaIni[1]),
		        DateTimeZone.forID(ZONA_HORARIA));
		DateTime horaF = new DateTime(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]),
				Integer.parseInt(fecha[2]), Integer.parseInt(horaFin[0]), Integer.parseInt(horaFin[1]),
		        DateTimeZone.forID(ZONA_HORARIA));
		Usuario organizador = (Usuario) session.getAttribute("user");
		String url = jso.getString("url");
		String[] correosAsistentes = ((jso.getString(CORREOS)).replace(" ", "")).split(",");
		return Reunion.get().guardarReunion(titulo, descripcion, horaI, horaF, organizador, correosAsistentes, url);

	}

	@PostMapping("/loadReunion")
	public String loadReunion(HttpSession session, @RequestBody Map<String, Object> loadReunion) throws Exception {
		JSONObject jso = new JSONObject(loadReunion);
		String id = jso.getString("id");
		JSONObject jsoReunion = Reunion.get().loadReunion(id);
		Usuario usuario = (Usuario) session.getAttribute("user");
		jsoReunion.put("userSesion", usuario.getUser());
		return jsoReunion.toString();
	}

	@PostMapping("/loadUser")
	public String loadUser(@RequestBody Map<String, Object> loadUser) {
		JSONObject jso = new JSONObject(loadUser);
		String username = jso.getString(USER_NAME);
		Optional<Usuario> optUser = userdao.findById(username);
		if (optUser.isPresent()) {
			return optUser.get().toJSON().toString();
		} else
			return null;

	}

	@PostMapping("/getReuniones")
	public String getReuniones(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("user");
		return Reunion.get().getReuniones(usuario).toString();
	}

	@PostMapping("/getUsuarios")
	public String getUsuarios(HttpSession session) {
		JSONObject jso = new JSONObject();
		jso.put("usuarios", Usuario.get().getUsuarios());
		return jso.toString();
	}

	@PostMapping("/getRol")
	public String getRol(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("user");
		JSONObject jso = new JSONObject();
		jso.put("rol", usuario.getRol());
		Usuario.get().getUsuarios();
		return jso.toString();
	}

	@PostMapping("/recibirInvitacion")
	public String getInvitaciones(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("user");
		JSONObject jso = new JSONObject();
		jso.put("invitaciones", Invitacion.get().recibirInvitacion(usuario));
		return jso.toString();
	}

	@PostMapping("/nuevaInvitacion")
	public void nuevaInvitacion(HttpSession session, @RequestBody Map<String, Object> correos) throws Exception {
		JSONObject jso = new JSONObject(correos);
		String id = jso.getString("id");
		String[] correosAsistentes = ((jso.getString(CORREOS)).replace(" ", "")).split(",");
		Invitacion.get().enviarInivitacion(id, correosAsistentes);

	}

	@PostMapping("/responderInvitacion")
	public String responderInvitacion(HttpSession session, @RequestBody Map<String, Object> opcionesInvitacion)
	        throws Exception {
		Usuario usuario = (Usuario) session.getAttribute("user");
		JSONObject jso = new JSONObject(opcionesInvitacion);

		String idInv = jso.getString("idInv");
		boolean opcion = jso.getBoolean("opcion");

		JSONObject resp = new JSONObject();
		resp.put("msg", Invitacion.get().responderInvitacion(usuario, idInv, opcion));
		return resp.toString();

	}

	@PostMapping("/modifyUser")
	public void modificar(HttpSession session, @RequestBody Map<String, Object> credenciales) {
		
		JSONObject jso = new JSONObject(credenciales); 
		String userName = jso.getString(USER_NAME); 
		String nombre = jso.getString("userCompletName");
		String userApellidos =jso.getString("userApellidos"); 
		String userDni = jso.getString("userDni"); 
		int userTelf = Integer.parseInt(jso.getString("userTelf")); 
		String userMail = jso.getString("userMail"); 
		String rol = jso.getString("rol");
		JSONObject resultado = new JSONObject();

		
		try { 
			Usuario.get().modificarUsuario(userName, nombre, userApellidos, userDni, userTelf, userMail, rol);
			resultado.put("type", "OK");
		}catch (Exception e) { 
			resultado.put("type", error); 
			resultado.put(message, e.getMessage()); 
		}

	}

	@PostMapping("/eliminarUsuario")
	public void eliminarUsuario(HttpSession session, @RequestBody Map<String, Object> datosUsuario) {
		JSONObject jso = new JSONObject(datosUsuario);
		String idUsuario = jso.getString("id");
		Usuario.get().eliminarUsuario(idUsuario);
	}

	@PostMapping("/getMyAccount")
	public String getMyAccount(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("user");
		return Usuario.get().getMyAccount(usuario).toString();
	}

	@PostMapping("/eliminarReunionUsuario")
	public void eliminarReunionUsuario(HttpSession session, @RequestBody Map<String, Object> reunion) {
		Usuario usuario = (Usuario) session.getAttribute("user");
		JSONObject jso = new JSONObject(reunion);
		String idReunion = jso.getString("idReunion");
		Reunion.get().eliminarReunionUsuario(usuario, idReunion);
	}

	@PostMapping("/modificarReunion")
	public void modificarReunion(HttpSession session, @RequestBody Map<String, Object> datosModificados)
	        throws Exception {
		
		JSONObject jso = new JSONObject(datosModificados);
		 String id = jso.getString("id");
		 String nombreReunion = jso.getString("nombre");
		 String[] fecha = jso.getString("fecha").split("-");
		 String[] horaI = jso.getString("horaInicio").split(":");
		 String[] horaF = jso.getString("horaFin").split(":");
		 String descripcion = jso.getString("descripcion");
		 String url = jso.getString("url");
	     String[] correosAsistentes = ((jso.getString(CORREOS)).replace(" ", "")).split(",");
	     DateTime horaInicio = new DateTime(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]),Integer.parseInt(horaI[0]), Integer.parseInt(horaI[1]), DateTimeZone.forID(ZONA_HORARIA));
	     DateTime horaFin = new DateTime(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]),Integer.parseInt(horaF[0]), Integer.parseInt(horaF[1]), DateTimeZone.forID(ZONA_HORARIA));
	     Reunion.get().modificarReunion(id,nombreReunion,horaInicio,horaFin,descripcion,url,correosAsistentes);


	}
	
	@PostMapping("/cerrarSesion")
	public void cerrarSesion(HttpSession session){
		session.invalidate();
	}
}
