$(document).ready(function() {
    $('input[id=pwd1]').keyup(function() {
        var pswd = $(this).val();
        //length
        if ( pswd.length < 8 ) {
            $('#length').removeClass('valid').addClass('invalid');
        } else {
            $('#length').removeClass('invalid').addClass('valid');
        }
        //letter
        if ( pswd.match(/[A-z]/) ) {
            $('#letter').removeClass('invalid').addClass('valid');
        } else {
            $('#letter').removeClass('valid').addClass('invalid');
        }
        //validate number
        if ( pswd.match(/\d/) ) {
            $('#number').removeClass('invalid').addClass('valid');
        } else {
            $('#number').removeClass('valid').addClass('invalid');
        }
        //mayus
        if ( pswd.match(/[A-Z]/) ) {
            $('#capital').removeClass('invalid').addClass('valid');
        } else {
            $('#capital').removeClass('valid').addClass('invalid');
        }

    }).focus(function() {
        $('#alertPass').show();
    }).blur(function() {
        $('#alertPass').hide();
    });
});

    function comprobar(){
    	restaurarSenalizacion();
        var mail1 = $("#userMail").val();
        var username = $("#userName").val();
        var pass=$("#pwd1").val();
        var pass2=$("#pwd2").val();
        var mail2=$("#mail2").val();
        
        var continuar = 0;
        while(continuar < 10){
        	console.log("He entrado en el bucle");
        	continuar = 0;
        	if(false != validarCampo("userMail")){
        		continuar++;
        	} else if(false != validarCampo("userName")){
        		continuar++;
        	} else if(false != validarCampo("pwd1")){
        		continuar++;
        	} else if(false != validarCampo("mail2")){
        		continuar++;
        	} else if(false != validarCampo("userCompletName")){
        		continuar++;
        	} else if(false != validarCampo("userApellidos")){
        		continuar++;
        	} else if(false != validarCampo("userTelf")){
        		continuar++;
        	} else if(false != validarCampo("userDate")){
        		continuar++;
        	} else if(false != validarCampo("userDni")){
        		continuar++;
        	} else if(false != validarCampo("pwd2")){
        		continuar++;
        	} else if(false != validarCampo("mail2")){
        		continuar++;
        	}
        	console.log("He pasado por aquí y continuar es "+ continuar);
        }
        
        console.log("He sudado del bucle");
        
        if(pass!==pass2){
        		alert("Las contraseñas no puede ser distintas");
        	}else if(mail1!==mail2){
        		alert("Los e-mails no pueden ser distintos");
        		}
        	else if(userTelf.value.length>9)
        		alert("El numero de telefono es demasiado largo");
        	else if(!fechaCorrecta())
        		alert("Debe tener al menos 16 años");
        	else 
        		register();      
    }
    
    function restaurarSenalizacion(){
    	document.getElementById("userCompletName").style.border = "1px solid grey";
    	document.getElementById("userDni").style.border = "1px solid grey";
    	document.getElementById("userApellidos").style.border = "1px solid grey";
    	document.getElementById("userTelf").style.border = "1px solid grey";
    	document.getElementById("userMail").style.border = "1px solid grey";
    	document.getElementById("pwd1").style.border = "1px solid grey";
    	document.getElementById("pwd2").style.border = "1px solid grey";
    	document.getElementById("userDate").style.border = "1px solid grey";
    	
    }
    
    function fechaCorrecta() {// el emleado tiene al menos 16 años
	    var correcto = new Boolean(true); 
	    var fecha=$("#userDate").val();
	    var diaFormulario = fecha.substring(8,10);
	    var mesFormularo = fecha.substring(5,7);
	    var añoFormulario = fecha.substring(0,4);
	    var edad = 16;
	    var fechaFormulario = new Date();
	    fechaFormulario.setFullYear(añoFormulario, mesFormularo-1, diaFormulario);

	    var fechaHoy = new Date();

	    fechaHoy.setFullYear(fechaHoy.getFullYear() - edad);

	     if (fechaHoy <fechaFormulario)
	       correcto=false;
	    return correcto;
	  }

    function validarCampo(campo) { // comprobar que no hay campos vacios
        var valido = new Boolean(true); 
        if (document.getElementById(campo).value == '') {
            switch(String(campo)){
                case 'userCompletName':
                    alert('El nombre completo no puede estar vacío');
                    document.getElementById("userCompletName").style.border = "2px solid red";
                    break;
                case 'userDni':
                    alert('El DNI no puede estar vacío');
                    document.getElementById("userDni").style.border = "2px solid red";
                    break;
                case 'userName':
                    alert('El nombre no puede estar vacío');
                    document.getElementById("userName").style.border = "2px solid red";
                    break;
                case 'userApellidos':
                    alert('El apellido no puede estar vacío');
                    document.getElementById("userApellidos").style.border = "2px solid red";
                    break;
                case 'userTelf':
                    alert('El teléfono no puede estar vacío');
                    document.getElementById("userTelf").style.border = "2px solid red";
                    break;
                case 'userMail':
                    alert('El email no puede estar vacío');
                    document.getElementById("userMail").style.border = "2px solid red";
                    break;
                case 'pwd1':
                    alert('La contraseña no puede estar vacía');
                    document.getElementById("pwd1").style.border = "2px solid red";
                case 'pwd2':
                    alert('La confirmación de la contraseña no puede estar vacía');
                    document.getElementById("pwd2").style.border = "2px solid red";
                    break;
                case 'userDate':
                    alert('La fecha de nacimiento no puede estar vacía');
                    document.getElementById("userDate").style.border = "2px solid red";
                    break;
                case 'mail2':
                    alert('La confirmación del email no puede estar vacía');
                    document.getElementById("mail2").style.border = "2px solid red";
                    break;
                default:
                    alert('El campo ' +campo +' está vacio');
            }
            valido = false;
        }
        return valido;
      };
      
      
    function limpiarCampos(){ // resetear todos los campos
        document.getElementById("userCompletName").value="";
        document.getElementById("userDni").value="";
        document.getElementById("userName").value="";
        document.getElementById("userApellidos").value="";
        document.getElementById("userTelf").value="";
        document.getElementById("userMail").value="";
        document.getElementById("pwd1").value="";
        document.getElementById("userDate").value="";
        document.getElementById("pwd2").value="";
        document.getElementById("mail2").value="";
    };
    
    function register(){

        var info = {
            type : "Register",
            userCompletName : userCompletName.value,
            userName : userName.value,
            userApellidos : userApellidos.value,
            userDni : userDni.value,
            userDate : userDate.value,
            userTelf : userTelf.value,
            userMail : userMail.value,
            pwd1 : pwd1.value,
            pwd2 : pwd2.value
        };
        var data = {
                data : JSON.stringify(info),
                url : "register",
                type : "post",
                contentType: 'application/json',
                success: function(response){
                	var respuesta = JSON.parse(response);
                	if(respuesta.type==="OK"){
                		alert("Usuario registrado correctamente");
                    	limpiarCampos();
                    	window.location="Login.html";
                	}
                	else
                		alert("Error: El nombre de usuario ya existe");
                },
                error : function(response) {
                	var respuesta = JSON.parse(response);
                	alert(respuesta.message);
                	
                }
            };
            $.ajax(data);
    }
