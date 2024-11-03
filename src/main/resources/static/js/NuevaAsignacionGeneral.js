// Obtener información de respuesta.
var materias = JSON.parse(materiasJSON);
var asignaciones = JSON.parse(asignacionesJSON);
var primeros = JSON.parse(primerosJSON);
var segundos = JSON.parse(segundosJSON);
var terceros = JSON.parse(tercerosJSON);

// Función para manejar el cambio de selección
document.getElementById('idMateria').addEventListener('change', function() {
    
    var materiaSeleccionada = this.value;
    var materia = materias.filter(materia => materia.idMateria == materiaSeleccionada)[0];

    var contenedoresP = document.getElementById('cbxPrimeros');
    var contenedoresS = document.getElementById('cbxSegundos');
    var contenedoresT = document.getElementById('cbxTerceros');

    if(materiaSeleccionada == ''){
        restoreProfesorSelect();
        restoreTextoAnosText('cbxTodosPrimeros');
        restoreTextoAnosText('cbxTodosSegundos');
        restoreTextoAnosText('cbxTodosTerceros');
        contenedoresP.innerHTML = '';
        contenedoresS.innerHTML = '';
        contenedoresT.innerHTML = ''
    }
    else{
        // Filtrar las asignaciones a grados que ya se le ha asignado
        var asignacionesPrimeros = asignaciones.filter(asignacion => 
            asignacion.materia.idMateria == materiaSeleccionada && asignacion.bachillerato.grado == 1
        );  
        var asignacionesSegundos = asignaciones.filter(asignacion => 
            asignacion.materia.idMateria == materiaSeleccionada && asignacion.bachillerato.grado == 2
        ); 
        var asignacionesTerceros = asignaciones.filter(asignacion => 
            asignacion.materia.idMateria == materiaSeleccionada && asignacion.bachillerato.grado == 3
        );  

        // Quitar bachilleratos ya asignados
        var primerosFiltrados = quitarBachilleratos(primeros, asignacionesPrimeros, 'codigoBachillerato');
        var segundosFiltrados = quitarBachilleratos(segundos, asignacionesSegundos, 'codigoBachillerato');
        var tercerosFiltrados = quitarBachilleratos(terceros, asignacionesTerceros, 'codigoBachillerato');
        
        // Mostrar los resultados filtrados en las secciones correspondientes
        if (primerosFiltrados.length != 0) {
            var p = 'primeros-anos';
            agregarCbxTodos('cbxTodosPrimeros', p, "marcarTodosPrimerosAnos", 'Todos los primeros años');
            mostrarResultados(contenedoresP, primerosFiltrados, p);
        }
        else{
            restoreTextoAnosText('cbxTodosPrimeros');
        }
    
        if (segundosFiltrados.length != 0)  {
            var s = 'segundos-anos';
            agregarCbxTodos('cbxTodosSegundos', s, "marcarTodosSegundosAnos", 'Todos los segundos años');
            mostrarResultados(contenedoresS, segundosFiltrados, s);
        }
        else{
            restoreTextoAnosText('cbxTodosSegundos');
        }

        if(tercerosFiltrados.length != 0) {
            // Asegúrate de que el objeto materia tiene la propiedad tipoMateria
            if (materia && materia.tipoMateria !== "Básica") {
                var t = 'terceros-anos';
                createTercerosAnosTab();
                agregarCbxTodos('cbxTodosTerceros', t, "marcarTodosTercerosAnos", 'Todos los terceros años');
                mostrarResultados(contenedoresT, tercerosFiltrados, t);
            } else {
                contenedoresT.innerHTML = '';
                removeTercerosAnosTab();
            }
        }
        else{
            restoreTextoAnosText('cbxTodosTerceros');
        }

    }
});

// Función para quitar los bachilleratos de la lista principal que ya están en la lista a eliminar
function quitarBachilleratos(listaPrincipal, listaAEliminar, propiedad) {
    return listaPrincipal.filter(itemPrincipal => 
        !listaAEliminar.some(itemAEliminar => itemPrincipal[propiedad] === itemAEliminar.bachillerato[propiedad])
    );
}

function agregarCbxTodos(cbxNombre, grupo, marca, texto){
    var contenedorPrincipal = document.getElementById(cbxNombre);
    contenedorPrincipal.innerHTML="";
    var input = document.createElement('input');
    input.className = 'form-check-input';
    input.classList.add("marcar-todos");
    input.classList.add("checkbox-group");
    input.type = 'checkbox';
    input.id = marca;
    input.setAttribute('data-group', grupo); // Agregar el atributo data-group

    var label = document.createElement('label');
    label.className = 'form-check-label label-checkbox-group';
    label.setAttribute('for', marca); // Vincula el label al input
    label.innerHTML = texto;

    contenedorPrincipal.appendChild(input);
    contenedorPrincipal.appendChild(label);
}

// Función para mostrar los resultados filtrados en las secciones correspondientes
function mostrarResultados(seccion, listaFiltrada, tipoanio) {
    seccion.innerHTML = ''; // Limpiar contenido previo

    listaFiltrada.forEach(item => {
        var div = document.createElement('div');
        div.className = 'col-md-6 mb-3 form-check form-switch';

        var input = document.createElement('input');
        input.className = 'form-check-input';
        input.classList.add(tipoanio);
        input.classList.add("checkbox-group");
        input.type = 'checkbox';
        input.name = 'codigoBachillerato';
        input.id = item.codigoBachillerato;
        input.value = item.codigoBachillerato;

        var label = document.createElement('label');
        label.className = 'form-check-label label-checkbox-group';
        label.innerHTML = `<span>${item.nombreCarrera}</span ><br /><span id="seccionSpan">Sección: ${item.seccion}</span>`;

        div.appendChild(input);
        div.appendChild(label);
        seccion.appendChild(div);
    });
}

$(document).ready(function() {
    $('#idMateria').change(function() {
        var idMateria = $(this).val();
        if (idMateria != "") {
            $.ajax({
                url: '/DocentesMax',
                type: 'GET',
                data: { idMateria: idMateria },
                success: function(response) {
                    var docentesHtml = '<option value="">Seleccionar docente...</option>';
                    response.forEach(function(docente) {
                        docentesHtml += '<option value="' + docente.duiDocente + '">' + docente.nombreDocente + ' ' + docente.apellidoDocente + '</option>';
                    });
                    $('#profesor').html(docentesHtml);
                },
                error: function(error) {
                    $('#profesor').html('<option value="">Error al cargar los docentes</option>');
                }
            });
        }
    });
});

function createTercerosAnosTab() {
    // Verificar si el tab ya existe
    if (!document.getElementById('terceros-anos-tab')) {
        // Crear el nuevo elemento li
        const li = document.createElement('li');
        li.className = 'nav-item';
        li.setAttribute('role', 'presentation');
        
        // Crear el botón dentro del li
        const button = document.createElement('button');
        button.className = 'nav-link';
        button.id = 'terceros-anos-tab';
        button.setAttribute('data-bs-toggle', 'tab');
        button.setAttribute('data-bs-target', '#terceros-anos');
        button.setAttribute('type', 'button');
        button.setAttribute('role', 'tab');
        button.setAttribute('aria-controls', 'terceros-anos');
        button.setAttribute('aria-selected', 'false');
        button.textContent = 'Terceros años';
        
        // Añadir el botón al li
        li.appendChild(button);
        
        // Añadir el nuevo li al ul con id menuTabs
        document.getElementById('menuTabs').appendChild(li);
    }
}

function removeTercerosAnosTab() {
    // Buscar el botón con id terceros-anos-tab
    const tab = document.getElementById('terceros-anos-tab');
    if (tab) {
        // Verificar si el tab está actualmente activo
        if (tab.classList.contains('active')) {
            // Redirigir al usuario al tab de "Primeros años"
            document.getElementById('primeros-anos-tab').click();
        }
        // Eliminar el li que contiene el botón
        tab.parentNode.remove();
    }
}

function restoreTextoAnosText(cbx) {
    const cbxTexto = document.getElementById(cbx);
    if (cbxTexto) {
        if (cbx == 'cbxTodosPrimeros') {
            cbxTexto.textContent = "No se encuentran primeros años por asignar.";
            limpiarCbxPrimeros();
        } 
        else if (cbx == 'cbxTodosSegundos') {
            cbxTexto.textContent = "No se encuentran segundos años por asignar.";
            limpiarCbxSegundos();
        }
        else if (cbx == 'cbxTodosTerceros') {
            cbxTexto.textContent = "No se encuentran terceros años por asignar.";
            limpiarCbxTerceros(); 
        }
        
    }
}

function restoreProfesorSelect() {
    const profesorSelect = document.getElementById('profesor');
    if (profesorSelect) {
        profesorSelect.innerHTML = `
            <option value="">Seleccionar docente...</option
        `;
    }
}

// Función para limpiar los cbx de primer año
function limpiarCbxPrimeros() {
    var contenedorP = document.getElementById('cbxPrimeros');
    if (contenedorP) {
        contenedorP.innerHTML = ''; // Limpiar el contenido del contenedor de primeros años
    }
}

// Función para limpiar los cbx de segundo año
function limpiarCbxSegundos() {
    var contenedorS = document.getElementById('cbxSegundos');
    if (contenedorS) {
        contenedorS.innerHTML = ''; // Limpiar el contenido del contenedor de segundos años
    }
}

// Función para limpiar los cbx de tercer año
function limpiarCbxTerceros() {
    var contenedorT = document.getElementById('cbxTerceros');
    if (contenedorT) {
        contenedorT.innerHTML = ''; // Limpiar el contenido del contenedor de terceros años
    }
}
