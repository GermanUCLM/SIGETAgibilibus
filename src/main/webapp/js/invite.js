var data;
function invitar() {
	if (validarCampo("correos")) {
		var msg = {
			type : "invitacion",
			id : $("#idReunion").val(),
			correos : $("#correos").val()
		};
		data = {
			data : JSON.stringify(msg),
			url : "nuevaInvitacion",
			type : "post",
			contentType : 'application/json',
			dataType : 'json'
		}

	}
	$.ajax(data);
	document.getElementById("correos").value = "";
	alert('Invitación enviada');
}

function validarCampo(campo) { // comprobar que no hay campos vacios
	var valido = new Boolean(true);
	if (document.getElementById(campo).value == '') {
		alert('El campo ' + campo + ' está vacio');
		valido = false;
	}
	return valido;
}

function fechaCorrecta() {
    var correcto = new Boolean(true); 
    var fecha=$("#fecha").val();
    var fechaActual = new Date();
    var hora=$("#horaInicio").val();
    var fechaFormulario= new Date(fecha.substring(0,4),fecha.substring(5,7)-1,fecha.substring(8,10),hora.substring(0,2),hora.substring(3,5));
    if(false==(fechaFormulario >= fechaActual )){ // la fecha del formulario es menor a la actual (en este momento)
        alert('La fecha seleccionada es anterior a la actual');
        correcto=false;
    }

    if($("#horaInicio").val() > $("#horaFin").val() ){ //la hora de inicio es mayor que la de fin
        correcto=false;
        alert("La hora de fin no puede ser anterior a la de inicio")
    }
    return correcto;
  }

function modificar() {
	if (validarCampo("fecha") && validarCampo("horaFin") && validarCampo("horaInicio") && validarCampo("nombreReunion") && fechaCorrecta()) {
	var msg = {
		type : "modificar Reunion",
		id : $("#idReunion").val(),
		nombre : $("#nombreReunion").val(),
		fecha : $("#fecha").val(),
		horaInicio : $("#horaInicio").val(),
		horaFin : $("#horaFin").val(),
		descripcion : $("#descripcion").val(),
		url : $("#url").val(),
		correos : $("#correos").val()
	};
	var data = {
		data : JSON.stringify(msg),
		url : "modificarReunion",
		type : "post",
		contentType : 'application/json',
		dataType : 'json'
		}
		alert('Se ha modificado la reunión');
	}
		$.ajax(data);
		document.getElementById("correos").value = "";
	}


function asistir() {
	var opcion = confirm("¿Estás seguro que no desea asisitir a esta reunión?");
	
	if (opcion == true) {
		var msg = {
			type : "eliminar Reunion",
			idReunion : $("#idReunion").val()
		};
		var data = {
			data : JSON.stringify(msg),
			url : "eliminarReunionUsuario",
			type : "post",
			contentType : 'application/json',
			dataType : 'json'
		}
		$.ajax(data);
		alert('Usted dejó de asistir a la reunión');
	} else {
		window.location.href = "UserCalendar.html";
	}

}

