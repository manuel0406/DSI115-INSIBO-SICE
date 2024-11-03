document.addEventListener("DOMContentLoaded", function () {
  let selectedFile;
  const cargaButton = document.getElementById('idCarga');
  const spinner = document.createElement('span');
  const loadingText = document.createElement('span');

  // Configuración del spinner y texto de carga
  spinner.className = 'spinner-border spinner-border-sm';
  spinner.setAttribute('role', 'status');
  spinner.setAttribute('aria-hidden', 'true');

  loadingText.className = 'sr-only';
  loadingText.textContent = ' Loading...';

  // Listener para el cambio de archivo
  document.getElementById("input-excel").addEventListener("change", (e) => {
      selectedFile = e.target.files[0];
  });

  // Listener para el botón de carga
  cargaButton.addEventListener("click", () => {
      if (selectedFile) {
          handleFile();
      } else {
          Swal.fire({
              position: "center",
              icon: "error",
              title: "Error de archivo",
              text: "Por favor, seleccione un archivo primero.",
              showConfirmButton: false,
              timer: 5000
          });
      }
  });

  // Función para manejar el archivo seleccionado
  function handleFile() {
      // Cambiar el botón a estado de carga
      cargaButton.disabled = true; // Deshabilitar el botón mientras carga
      cargaButton.innerHTML = ''; // Limpiar el contenido del botón
      cargaButton.appendChild(spinner); // Mostrar el spinner
      cargaButton.appendChild(loadingText); // Mostrar el texto de carga

      const reader = new FileReader();
      reader.onload = function (event) {
          try {
              const data = new Uint8Array(event.target.result);
              const workbook = XLSX.read(data, { type: "array" });
              const sheetNameToFind = "Estadísticas de anormalías"; // Hoja buscada

              if (workbook.SheetNames.includes(sheetNameToFind)) {
                  const sheet = workbook.Sheets[sheetNameToFind];
                  const json = XLSX.utils.sheet_to_json(sheet, { header: 1 });
                  let results = [];
                  let columnIndexID = -1;

                  // Función para validar tiempo en formato HH:mm
                  function isValidTime(value) {
                      return /^([01]\d|2[0-3]):([0-5]\d)$/.test(value);
                  }

                  // Función para formatear fecha
                  function formatDate(dateStr) {
                      const [day, month, year] = dateStr.split("/");
                      return `${year}-${month}-${day}`;
                  }

                  // Función para formatear tiempo a dos dígitos
                  function formatTimeToTwoDigits(timeStr) {
                      return timeStr.length === 4 ? `0${timeStr}` : timeStr;
                  }

                  // Encuentra la columna con el título "ID"
                  for (let i = 0; i < json.length; i++) {
                      let row = json[i];
                      columnIndexID = row.indexOf("ID.");
                      if (columnIndexID !== -1) {
                          // Encontró la columna "ID"
                          for (let j = i + 1; j < json.length; j++) {
                              let dataRow = json[j];
                              let id = dataRow[columnIndexID];
                              if (id !== undefined) {
                                  let adjacentData = [];
                                  for (let k = 0; k <= 7; k++) {
                                      adjacentData.push(dataRow[columnIndexID + k]);
                                  }

                                  // Formatear los datos según el formato deseado
                                  const formattedDate = formatDate(adjacentData[3]);

                                  // Verificar si los campos de "Inicio" y "Fin" periodo 1 están presentes
                                  if (
                                      (adjacentData[4] || adjacentData[4] === "Falta") &&
                                      (adjacentData[5] || adjacentData[5] === "Falta")
                                  ) {
                                      const formattedInicioMatutino = adjacentData[4] && adjacentData[4] !== "Falta" 
                                          ? `${formattedDate}T${formatTimeToTwoDigits(adjacentData[4])}:00` 
                                          : `${formattedDate}T01:00:00`;
                                      const formattedFinMatutino = adjacentData[5] && adjacentData[5] !== "Falta" 
                                          ? `${formattedDate}T${formatTimeToTwoDigits(adjacentData[5])}:00` 
                                          : `${formattedDate}T01:00:00`;

                                      // Agregar datos para periodo solo si Inicio o Fin están presentes
                                      results.push({
                                          ID: id,
                                          Depart: adjacentData[2],
                                          Turno: "Matutino",
                                          Inicio: formattedInicioMatutino,
                                          Fin: formattedFinMatutino,
                                      });
                                  }

                                  // Verificar si los campos de "Inicio" y "Fin" para periodo 2 están presentes
                                  if (
                                      (adjacentData[6] || adjacentData[6] === "Falta") &&
                                      (adjacentData[7] || adjacentData[7] === "Falta")
                                  ) {
                                      const formattedInicioVespertino = adjacentData[6] && adjacentData[6] !== "Falta" 
                                          ? `${formattedDate}T${formatTimeToTwoDigits(adjacentData[6])}:00` 
                                          : `${formattedDate}T01:00:00`;
                                      const formattedFinVespertino = adjacentData[7] && adjacentData[7] !== "Falta" 
                                          ? `${formattedDate}T${formatTimeToTwoDigits(adjacentData[7])}:00` 
                                          : `${formattedDate}T01:00:00`;

                                      // Agregar datos periodo 2 solo si Inicio o Fin están presentes
                                      results.push({
                                          ID: id,
                                          Depart: adjacentData[2],
                                          Turno: "Vespertino",
                                          Inicio: formattedInicioVespertino,
                                          Fin: formattedFinVespertino,
                                      });
                                  }
                              }
                          }
                          break;
                      }
                  }

                  if (columnIndexID === -1) {
                      console.log("No se encontró la columna 'ID'.");
                  }
                  enviarDatos(results); // Envia los datos al servidor
              } else {
                  Swal.fire({
                      position: "center",
                      icon: "error",
                      title: "Error de procesamiento.",
                      text: "La hoja con el nombre \"Estadísticas de anormalías\" no se encontró en el archivo.",
                      showConfirmButton: false,
                      timer: 5000
                  });
              }
          } catch (error) {
              console.error("Error al procesar el archivo:", error);
              Swal.fire({
                  position: "center",
                  icon: "error",
                  title: "Error de procesamiento",
                  text: "Hubo un error al procesar el archivo. Por favor, inténtalo de nuevo.",
                  showConfirmButton: false,
                  timer: 5000
              });
          } finally {
              // Restaurar el estado original del botón
              cargaButton.disabled = false; // Habilitar el botón
              cargaButton.innerHTML = 'Cargar Archivo'; // Restaurar el texto del botón
          }
      };
      reader.readAsArrayBuffer(selectedFile);
  }

  // Función para enviar los datos al servidor
  function enviarDatos(datos) {
      const validDatos = datos.filter(dato => dato.Depart && dato.Depart.trim() !== "");
      if (validDatos.length === 0) {
          Swal.fire({
              position: "center",
              icon: "error",
              title: "Error de archivo",
              text: "No hay datos válidos para enviar.",
              showConfirmButton: false,
              timer: 5000
          });
          return;
      }

      const csrfToken = $('input[name="_csrf"]').val();
      console.log(csrfToken);

      $.ajax({
          url: "/asistencias/procesarAsistencias",
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify(validDatos),
          headers: {
              "X-XSRF-TOKEN": csrfToken // Agregar el token CSRF al encabezado
          },
          success: function (response) {
              console.log("Respuesta del servidor:", response);
              Swal.fire({
                  position: "center",
                  icon: "success",
                  title: response.mensaje,
                  showConfirmButton: false,
                  timer: 5000
              });
          },
          error: function (xhr, status, error) {
              console.error("Error al enviar datos:", error);
              Swal.fire({
                  position: "center",
                  icon: "error",
                  title: "Error de procesamiento",
                  text: "Error al enviar datos.",
                  showConfirmButton: false,
                  timer: 5000
              });
          }
      });
  }
});

// ARCHIVO DROPEADO
// Referencias
const dropzone = document.getElementById('dropzone');
const fileInput = document.getElementById('input-excel');
const fileInfo = document.getElementById('file-info');
const fileName = document.getElementById('file-name');
const fileUploadedIcon = document.getElementById('file-uploaded-icon');
const removeFileButton = document.getElementById('remove-file');
const inputExcelDiv = document.getElementById('inputExcel'); // Referencia al div inputExcel

// Tipos de archivos válidos (solo archivos de Excel)
const validFileTypes = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'];

// Función para validar tipo de archivo
function isValidFileType(file) {
    return validFileTypes.includes(file.type);
}

// Función para resetear el input de archivo
function resetFileInput() {
    fileInput.value = ''; // Vaciar el input de archivos
    fileInfo.classList.add('d-none'); // Ocultar la información del archivo cargado
    fileUploadedIcon.style.display = 'none'; // Ocultar la imagen de Excel
    inputExcelDiv.style.display = 'block'; // Mostrar el div inputExcel de nuevo
}

// Manejar el evento de cambio del input file
fileInput.addEventListener('change', () => {
    if (fileInput.files.length > 0) {
        const file = fileInput.files[0];

        // Validar si es un archivo de Excel
        if (isValidFileType(file)) {
            // Mostrar el nombre del archivo
            fileName.innerHTML = `<b>Archivo seleccionado:</b> ${file.name}`;            
            // Mostrar la imagen
            fileUploadedIcon.style.display = 'block';
            // Mostrar la sección de información del archivo
            fileInfo.classList.remove('d-none');
            // Ocultar el div inputExcel
            inputExcelDiv.style.display = 'none';
        } else {
            Swal.fire({
              position: "center",
              icon: "error",
              title: "Error de archivo",
              text: "Por favor, arrastra un archivo Excel válido (.xls o .xlsx)",
              showConfirmButton: false,
              timer: 5000
            });
            // Limpiar el input si no es un archivo válido
            resetFileInput();
        }
    }
});

// Manejador de archivos soltados
dropzone.addEventListener('drop', (event) => {
    event.preventDefault();
    dropzone.classList.remove('dragover'); // Remover clase cuando se suelta el archivo

    const files = event.dataTransfer.files;
    if (files.length > 0) {
        const file = files[0];

        // Validar si es un archivo de Excel
        if (isValidFileType(file)) {
            fileInput.files = files;
            fileInput.dispatchEvent(new Event('change')); // Disparar el evento 'change' manualmente
        } else {
            Swal.fire({
              position: "center",
              icon: "error",
              title: "Error de archivo",
              text: "Por favor, arrastra un archivo Excel válido (.xls o .xlsx)",
              showConfirmButton: false,
              timer: 5000
            });
        }
    }
});

// Manejador del botón para eliminar el archivo cargado
removeFileButton.addEventListener('click', () => {
    resetFileInput(); // Resetear el input de archivos y la interfaz
});

// Previene el comportamiento por defecto para permitir el drop
dropzone.addEventListener('dragover', (event) => {
    event.preventDefault();
    dropzone.classList.add('dragover'); // Agregar clase para cambiar estilo al hacer drag
});

// Remueve la clase cuando el archivo no está sobre el área
dropzone.addEventListener('dragleave', () => {
    dropzone.classList.remove('dragover');
});
