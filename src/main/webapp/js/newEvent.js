var data;
function crear () { // se le llama cuando se da al boton crear
    
    if((validarCampo("nombreReunion") && validarCampo("fecha") && validarCampo("horaInicio")&& validarCampo("horaFin") && validarCampo("descripcion") && validarCampo("url") && validarCampo("invitar"))){ // no hay un campo vacio
        if(fechaCorrecta()){ //se comprueba la fecha
            var reunion= new Reunion($("#nombreReunion").val(),$("#fecha").val(),$("#horaInicio").val(),$("#horaFin").val(),$("#descripcion").val(),$("#url").val(),$("#invitar").val()); // crear objeto reunion            
            reunion.crearReunion();
            alert('Se creó una reunion correctamente ');
            window.location.href = "UserCalendar.html";
            
            //limpiarCampos();
        }
    }
    $.ajax(data);
    
    
}
function validarCampo(campo) { // comprobar que no hay campos vacios
    var valido = new Boolean(true); 
    if (document.getElementById(campo).value == '') {
      alert('El campo ' +campo +' está vacio');
        valido=false;
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
    

function limpiarCampos(){ // resetear todos los campos
    document.getElementById("nombreReunion").value="";
    document.getElementById("fecha").value="";
    document.getElementById("horaInicio").value="";
    document.getElementById("horaFin").value="";
    document.getElementById("descripcion").value="";
    document.getElementById("url").value="";
    document.getElementById("invitar").value="";
}

function atras(){  
	window.location.href = "UserCalendar.html";
	//posiblemente quitar lo que hay detras
    /*var msg = {
        type : "atras",
        subtype: "crear reunion"
    }*/
    //mandar al controler convertir a json como en el metodo crear reunion
}


class Reunion{
    constructor(nombre, fecha, horaInicio, horaFin, descripcion, url,correos){
        this.nombre=nombre;
        this.fecha=fecha;
        this.horaInicio=horaInicio;
        this.horaFin=horaFin;
        this.descripcion=descripcion;
        this.url=url;
        this.correos=correos;
    }

    crearReunion(){
        var msg ={
            type : "nueva Reunion",
			nombre : this.nombre,
            fecha : this.fecha,
            horaInicio : this.horaInicio,
			horaFin : this.horaFin,
            descripcion : this.descripcion,
            url: this.url,
            correos: this.correos
        };
        data = {
            data : JSON.stringify(msg),
            url : "nuevaTarea",
            type : "post",
            contentType : 'application/json',
            dataType : 'json'
        }
      
    }
}