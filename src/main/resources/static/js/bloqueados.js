function irHacia(boton) {
    // Obtener el ID del botón y mostrarlo en la consola
    var idDelBoton = boton.textContent;
    // Redirigir a la página con el parámetro en la URL igual al ID del botón
    window.location.href = '/gestionarBloqueados?pagina=' + idDelBoton;
}

document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    let pagina = urlParams.get('pagina');
  
    if (pagina === null) {
      pagina = "1";
    }
    
    pagina = "pagina" + pagina;
  
    const button = document.getElementById(pagina);
    if (button) {
      button.classList.remove('btn-outline-dark');
      button.classList.add('btn-primary');
      button.classList.add('btnPaginado');
    }
});

document.getElementById('buscarForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Evitar el envío del formulario por defecto

  // Obtener el valor del campo de entrada y quitar espacios en blanco al inicio y al final
  var correo = document.getElementById('buscar').value.trim();
  var sinCorreo = document.getElementById('errorCorreo');

  // Validar si el campo está vacío
  if (correo === '') {
    sinCorreo.style.display = 'block';
    return; // Detener la ejecución si el campo está vacío
  }

  window.location.href = '/buscarUsuarioBloqueado?correoUsuario=' + correo;

});

function closeAlert() {
  var alertDiv = document.getElementById('errorAlert');
  alertDiv.style.display = 'none';
}

function closeCorreo() {
  var alertDiv = document.getElementById('errorCorreo');
  alertDiv.style.display = 'none';
}


document.addEventListener("DOMContentLoaded", function() {
  var AcceptButtons = document.querySelectorAll('.btn-InLockUser');
  var confirmAcceptModal = new bootstrap.Modal(document.getElementById('confirmInLockModal'));
  var confirmAcceptButton = document.getElementById('confirmInLockButton');
  var modalBody = document.querySelector('.body-modal-inLock');
  var currentHref = '';

  AcceptButtons.forEach(function(button) {
      button.addEventListener('click', function(event) {
          event.preventDefault(); // Evitar el comportamiento por defecto del botón
      
          // Obtener el atributo href del botón actual (si es necesario)
          currentHref = button.getAttribute('href');
      
          // Obtener la fila (tr) padre del botón actual
          var row = button.closest('tr');
          
          // Obtener el contenido de la columna 2 (índice 1 en base 0)
          var correo = row.cells[1].textContent.trim();
          
          // Modificar el texto del modal con el correo obtenido
          modalBody.textContent = "¿Deseas desbloquear al usuario " + correo + "?";
      
          // Mostrar el modal de confirmación
          confirmAcceptModal.show();
      });
  });

  confirmAcceptButton.addEventListener('click', function() {
      window.location.href = currentHref;
  });
});
