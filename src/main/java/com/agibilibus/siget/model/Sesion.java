package com.agibilibus.siget.model;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import com.agibilibus.siget.dao.UserDAO;

@Component
public class Sesion {

	@Autowired
	private UserDAO userdao;
	private Usuario user;
	private HttpSession httpSession;

	@Transient
	private ConcurrentHashMap<String, Usuario> connectedUsersByUserName;
	@Transient
	private ConcurrentHashMap<String, Usuario> connectedUsersByHttpSession;

	private Sesion() {
		this.connectedUsersByUserName = new ConcurrentHashMap<>();
		this.connectedUsersByHttpSession = new ConcurrentHashMap<>();
	}

	private Sesion(Usuario user, HttpSession sesion) {
		super();
		this.user = user;
		this.httpSession = sesion;

	}

	public boolean login(HttpSession httpSession, String userName, String pwd) throws Exception {
		String msg = "Credenciales inválidas";
		boolean correcto = false;
		try {

			Optional<Usuario> optUser = userdao.findById(userName);

			if (optUser.isPresent()) {
				user = optUser.get();
				if (user.getPassword().equals(pwd)) {
					correcto = true;
					Sesion sesion = new Sesion(user, httpSession);
					this.connectedUsersByUserName.put(userName, user);
					this.connectedUsersByHttpSession.put(httpSession.getId(), user);
					sesion.getHttpSession().setAttribute("user", user);

				} else {
					throw new Exception(msg);
				}
			} else {
				throw new Exception(msg);
			}

		} catch (SQLException e) {
			throw new Exception(msg);
		}
		return correcto;
	}

	private static class SesionHolder {
		static Sesion singleton = new Sesion();
	}

	@Bean(name = "beanSesion")
	public static Sesion get() {
		return SesionHolder.singleton;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}
}
