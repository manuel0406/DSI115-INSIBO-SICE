// Validación de formularios
(function () {
  "use strict";

  var forms = document.querySelectorAll(".needs-validation");

  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener(
      "submit",
      function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add("was-validated");
      },
      false
    );
  });

  // Función para limpiar el formulario
  function limpiarFormulario(formId) {
    var form = document.getElementById(formId);
    form.reset(); // Resetea el formulario
    form.classList.remove("was-validated"); // Remueve las clases de validación
  }

  // Mapa de botones y formularios
  const formularios = {
    btnCancelarDocente: "formDocente",
    cancelarDocente: "formDocente",
    btnCancelarAdministrativo: "formAdministrativo",
    cancelarAdministrativo: "formAdministrativo",
  };

  // Agregar event listeners para los botones de cancelar
  for (const [buttonId, formId] of Object.entries(formularios)) {
    document.getElementById(buttonId).addEventListener("click", function () {
      limpiarFormulario(formId);
    });
  }
})();

$(document).ready(function() {
  $('.editar-btn-docente').on('click', function() {
    var docenteNombre = $(this).data('docenteActual');
    var docenteId = $(this).data('docenteDui');
    var numeroAparatoDocente = $(this).data('numeroAparatoDocente');
    var idAparatoDocente = $(this).data('idAparatoDocente');

     $('#numAparatoDocente').val(numeroAparatoDocente);
     $('#docenteAsignado').val(docenteId);
     $('#idAparatoDocente').val(idAparatoDocente);
  });
});
$(document).ready(function() {
  $('.editar-btn-personal').on('click', function() {
    var personalNombre = $(this).data('personalActual');
    var personalId = $(this).data('personalDui');
    var numeroAparatoPersonal = $(this).data('numeroAparatoPersonal');
    var idAparatoPersonal = $(this).data('idAparatoPersonal');

     $('#numAparatoPersonal').val(numeroAparatoPersonal);
     $('#personalAsignado').val(personalId);
     $('#idAparatoPersonal').val(idAparatoPersonal);
  });
});


// ------------------------------------------------------------- //
//  VALIDACIONES DE NÙMERO DE IDENTIFICADORES UNICOS | AGREGAR   //
// ------------------------------------------------------------- //
// Selecciona los elementos necesarios
const numeroAparatoPersonalInput = document.getElementById('numeroAparatoPersonal');
const numeroAparatoDocenteInput = document.getElementById('numeroAparatoDocente');
const btnGuardarPersonal = document.getElementById('btnGuardarPersonal');
const btnGuardarDocente = document.getElementById('btnGuardarDocente');
const msgAdvertenciaDoc = document.getElementById('mensajeAdvertenciaDocente');
const msgAdvertenciaPer = document.getElementById('mensajeAdvertenciaPersonal');

const numeroAparatoActP = document.getElementById('numAparatoPersonal');
const numeroAparatoActD = document.getElementById('numAparatoDocente');
const btnActP = document.getElementById('btnActP');
const btnActD= document.getElementById('btnActD');
const msgAdvertenciaActP = document.getElementById('mensajeAdvertenciaActP');
const msgAdvertenciaActD = document.getElementById('mensajeAdvertenciaActD');

// Función general para verificar número y actualizar interfaz
function verificarNumero(inputElement, botonGuardar, mensajeAdvertencia) {
  const { value } = inputElement; // Desestructuración para obtener el valor
  const valorIngresado = parseInt(value, 10); // Convierte el valor a número

  const existeEnLista = aparato.includes(valorIngresado);
  
  botonGuardar.disabled = existeEnLista; // Desactiva el botón si existe
  mensajeAdvertencia.style.display = existeEnLista ? 'block' : 'none'; // Muestra u oculta el mensaje

  // Manejo de clases de feedback
  inputElement.classList.toggle('is-invalid', existeEnLista); // Activa la clase si existe
  inputElement.classList.toggle('is-valid', !existeEnLista); // Activa la clase si no existe
}

// Agrega el evento input para ambos campos con la función generalizada
numeroAparatoDocenteInput.addEventListener('input', () => {
  verificarNumero(numeroAparatoDocenteInput, btnGuardarDocente, msgAdvertenciaDoc);
});

numeroAparatoPersonalInput.addEventListener('input', () => {
  verificarNumero(numeroAparatoPersonalInput, btnGuardarPersonal, msgAdvertenciaPer);
});

numeroAparatoActP.addEventListener('input', () => {
  verificarNumero(numeroAparatoActP, btnActP, msgAdvertenciaActP);
});
numeroAparatoActD.addEventListener('input', () => {
  verificarNumero(numeroAparatoActD, btnActD, msgAdvertenciaActD);
});