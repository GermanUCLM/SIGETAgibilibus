package com.agibilibus.siget.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.agibilibus.siget.dao.InvitacionDAO;
import com.agibilibus.siget.dao.ReunionDAO;
import com.agibilibus.siget.dao.UserDAO;

import lombok.Data;

@Component
@Data
@Document(collection = "reuniones")
public class Reunion {
	@Id
	private String idReunion;
	private String titulo;
	private String descripcion;
	private DateTime horaInicio;
	private DateTime horaFin;
	private Usuario organizador;
	private List<Usuario> asistentes;
	private List<EstadoInvitacion> estadosInvitaciones;
	private String url;

	@Autowired
	private ReunionDAO reuniondao;

	@Autowired
	private UserDAO userdao;

	@Autowired
	private InvitacionDAO invitaciondao;

	public Reunion() {
	}

	public Reunion(String idReunion, String titulo, String descripcion, DateTime horaInicio, DateTime horaFin,
	        Usuario organizador, List<Usuario> asistentes, String url) {
		super();
		this.idReunion = idReunion;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.organizador = organizador;
		this.asistentes = asistentes;
		this.url = url;
	}

	public String getIdReunion() {
		return idReunion;
	}

	public void setIdReunion(String idReunion) {
		this.idReunion = idReunion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public DateTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(DateTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public DateTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(DateTime horaFin) {
		this.horaFin = horaFin;
	}

	public Usuario getOrganizador() {
		return organizador;
	}

	public void setOrganizador(Usuario organizador) {
		this.organizador = organizador;
	}

	public List<Usuario> getAsistentes() {
		return asistentes;
	}

	public void setAsistentes(List<Usuario> asistentes) {
		this.asistentes = asistentes;
	}

	public List<EstadoInvitacion> getEstadosInvitaciones() {
		return estadosInvitaciones;
	}

	public void setEstadosInvitaciones(List<EstadoInvitacion> estadosInvitaciones) {
		this.estadosInvitaciones = estadosInvitaciones;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject toJSON() {
		JSONObject jso = new JSONObject();
		JSONArray jsaAsistentes = new JSONArray();
		jso.put("id", this.idReunion);
		jso.put("title", this.titulo);
		jso.put("descripcion", this.descripcion);
		String horaInicioS = this.horaInicio.toString().substring(11, 19);
		String fecha = this.horaInicio.toString().substring(0, 10);
		String horaFinS = this.horaFin.toString().substring(11, 19);
		jso.put("fecha", fecha);
		jso.put("start", horaInicioS);
		jso.put("end", horaFinS);
		jso.put("organizador", this.organizador.getUser());
		for (Usuario u : this.asistentes)
			jsaAsistentes.put(u.toJSON());
		jso.put("asistentes", jsaAsistentes);
		jso.put("url", this.url);
		return jso;
	}

	public JSONObject toEvent() {
		JSONObject jsoEvento = new JSONObject();
		jsoEvento.put("id", this.idReunion);
		jsoEvento.put("title", this.titulo);
		jsoEvento.put("start", this.horaInicio);
		jsoEvento.put("end", this.horaFin);
		return jsoEvento;
	}
	
	// Formato para guardar el id:
	// Organizador#Titulo#HoraInicio#HoraFin#Asistente1#Asistente2....
	public Reunion guardarReunion(String titulo, String descripcion, DateTime horaInicio, DateTime horaFin,
	        Usuario organizador, String[] correosAsistentes, String url) {
		List<Usuario> asist = new ArrayList<>();
		Reunion r = null;
		String id = organizador.getUser() + "#" + titulo + "#" + horaInicio.toString() + "#" + horaFin.toString();		
		Optional<Usuario> optUser = userdao.findById(organizador.getUser());
		if (optUser.isPresent()) {
			Usuario or = optUser.get();
			r=new Reunion(id, titulo, descripcion, horaInicio, horaFin, or, asist, url);
			for(String asistente : correosAsistentes) {
				Optional<Usuario> a = userdao.findByEmail(asistente);
				if(a.isPresent()) {
					id += "#"+a.get().getUser();
					invitaciondao.save(new Invitacion(id, a.get(), r, EstadoInvitacion.PENDIENTE));
				}
			}
		}
		return reuniondao.insert(r);
	}

	public void eliminarReunion(Reunion r) {
		reuniondao.deleteById(r.getIdReunion());
	}

	public JSONArray getReuniones(Usuario u) {
		JSONArray jsaReuniones = new JSONArray();
		Optional<Usuario> optUser = userdao.findById(u.getUser());
		if (optUser.isPresent()) {
			Usuario usuario = optUser.get();
			List<Reunion> reuniones = reuniondao.findAll();
			for (Reunion r : reuniones) {
				if ((r.getOrganizador().getUser().equals(usuario.getUser())) || (r.getAsistentes().contains(usuario))) {
					jsaReuniones.put(r.toEvent());
				}
			}
		}
		return jsaReuniones;
	}

	public List<Reunion> getListReuniones(Usuario u) {
		List<Reunion> todasReuniones = reuniondao.findAll();
		List<Reunion> reunionesUsuario = new ArrayList<>();
		for (Reunion r : todasReuniones)
			if (r.getOrganizador().getUser().equals(u.getUser()) || r.getAsistentes().contains(u))
				reunionesUsuario.add(r);
		return reunionesUsuario;
	}

	private static class ReunionHolder {
		static Reunion singleton = new Reunion();
	}

	@Bean(name = "beanReunion")
	public static Reunion get() {
		return ReunionHolder.singleton;
	}

	public JSONObject loadReunion(String id) {
		Optional<Reunion> reunion = reuniondao.findById(id);
		Reunion r = new Reunion();
		if (reunion.isPresent()) {
			r = reunion.get();
		}
		return r.toJSON();
	}

	public void cambiarOrganizarReunion(Reunion reunion) {
		Optional<Reunion> optReunion = reuniondao.findById(reunion.getIdReunion());
		if (optReunion.isPresent()) {
			if (optReunion.get().getAsistentes().isEmpty())
				reuniondao.deleteById(reunion.getIdReunion());
			else {
				Reunion r = optReunion.get();
				r.setOrganizador(r.getAsistentes().get(0));
				r.getAsistentes().remove(0);
				reuniondao.save(r);
			}
		}
	}

	public void eliminarAsistenteReunion(Usuario u, Reunion r) {
		List<Usuario> asistentesReunion = r.getAsistentes();
		asistentesReunion.remove(asistentesReunion.indexOf(u));
		r.setAsistentes(asistentesReunion);
		Optional<Reunion> optReunion = reuniondao.findById(r.getIdReunion());
		if (optReunion.isPresent())
			reuniondao.save(r);
	}

	public void eliminarReunionUsuario(Usuario usuario, String idReunion) {
		Optional<Reunion> optReunion = reuniondao.findById(idReunion);
		if (optReunion.isPresent()) {
			Reunion r = optReunion.get();
			if (r.getOrganizador().getUser().equals(usuario.getUser()))
				cambiarOrganizarReunion(r);
			else if (r.getAsistentes().contains(usuario))
				eliminarAsistenteReunion(usuario, r);
			Invitacion i = Invitacion.get().getInvitacion(r, usuario);
			if (i != null)
				i.setEstado(EstadoInvitacion.RECHAZADO);
		}
	}

	public void modificarReunion(String id, String nombreReunion, DateTime horaI, DateTime horaF, String descripcion,
	        String url, String[] correosAsistentes) {

		Optional<Reunion> optReunion = reuniondao.findById(id);
		if (optReunion.isPresent()) {
			Reunion reunion = optReunion.get();
			reunion.setTitulo(nombreReunion);
			reunion.setDescripcion(descripcion);
			reunion.setAsistentes(asistentes(correosAsistentes, reunion.getAsistentes()));
			reunion.setUrl(url);
			reunion.setHoraInicio(horaI);
			reunion.setHoraFin(horaF);
			reuniondao.save(reunion);
		}
	}

	public List<Usuario> asistentes(String[] correoAsistentes, List<Usuario> asistentes) {
		Usuario user = null;
		for (String correo : correoAsistentes) {
			if (!correo.equals("")) {
				Optional<Usuario> usr = userdao.findByEmail(correo);
				if (usr.isPresent()) {
					user = usr.get();
					if (!asistentes.contains(user)) {
						asistentes.add(user);
					}
				}

			}
		}
		return asistentes;
	}
}
