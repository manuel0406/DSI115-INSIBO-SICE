function validarCorreo(inputId) { 
  // Obtener el campo de entrada y el elemento de error
  var input = document.getElementById(inputId);
  var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  // Verificar si el correo es válido
  if (regex.test(input.value)) {
    input.classList.remove('is-invalid');
    input.classList.add('is-valid');
  } else {
    input.classList.remove('is-valid');
    input.classList.add('is-invalid');
  }

  validateForm(); // Llamar a validateForm para controlar el botón de envío
}

// Llamar a la función validarCorreo cuando se pierde el foco
document
  .getElementById("correoIniciarSesion")
  .addEventListener("blur", function () {
    validarCorreo("correoIniciarSesion");
  });

// Validar al presionar la tecla Enter
document
  .getElementById("correoIniciarSesion")
  .addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
      event.preventDefault();
      validarCorreo("correoIniciarSesion");
      moveToNextInput(event);
    }
  });

function validateForm() {
  const correo = document.getElementById("correoIniciarSesion").value;
  const botonIniciar = document.getElementById("botonRecuperar");

  if (correo && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo)) {
    botonIniciar.disabled = false;
  } else {
    botonIniciar.disabled = true;
  }
}

//Muestra una barra de carga.
document.getElementById("botonRecuperar").addEventListener("click", function () {
  Swal.fire({
    title: "¡Procesado restauración de credenciales!",
    html: "Se está procesando la petición de restauración de usuario",
    allowOutsideClick: false, // Evita que se cierre al hacer clic fuera de la alerta
    didOpen: () => {
      Swal.showLoading(); // Muestra el indicador de carga
    }
  });
});