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

function irHacia(boton) {
  // Obtener el ID del botón y mostrarlo en la consola
  var idDelBoton = boton.textContent;
  // Redirigir a la página con el parámetro en la URL igual al ID del botón
  window.location.href = '/gestionarCredenciales?pagina=' + idDelBoton;
}