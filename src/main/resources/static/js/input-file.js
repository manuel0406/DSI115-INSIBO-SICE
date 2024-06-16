var isAdvancedUpload = (function () {
  var div = document.createElement("div");
  return (
    ("draggable" in div || ("ondragstart" in div && "ondrop" in div)) &&
    "FormData" in window &&
    "FileReader" in window
  );
})();

let draggableFileArea = document.querySelector(".drag-file-area");
let browseFileText = document.querySelector(".browse-files");
let uploadIcon = document.querySelector(".upload-icon");
let dragDropText = document.querySelector(".dynamic-message");
let fileInput = document.querySelector(".default-file-input");
let cannotUploadMessage = document.querySelector(
  ".cannot-upload-message"
);
let cancelAlertButton = document.querySelector(".cancel-alert-button");
let uploadedFile = document.querySelector(".file-block");
let fileName = document.querySelector(".file-name");
let fileSize = document.querySelector(".file-size");
let progressBar = document.querySelector(".progress-bar");
let removeFileButton = document.querySelector(".remove-file-icon");
let uploadButton = document.querySelector(".upload-button");
let fileFlag = 0;

fileInput.addEventListener("click", () => {
  fileInput.value = "";
  console.log(fileInput.value);
});

fileInput.addEventListener("change", (e) => {
  console.log(" > " + fileInput.value);
  uploadIcon.innerHTML = "!";
  dragDropText.innerHTML = "Archivo Agregado!";
  document.querySelector(
    ".label"
  ).innerHTML = `Suelta o busca<span class="browse-files"> <input type="file" class="default-file-input" style=""/> <span class="browse-files-text" style="top: 0;"> buscar archivo</span></span>`;
  uploadButton.innerHTML = `Subir`;
  fileName.innerHTML = fileInput.files[0].name;
  fileSize.innerHTML =
    (fileInput.files[0].size / 1024).toFixed(1) + " KB";
  uploadedFile.style.cssText = "display: flex;";
  progressBar.style.width = 0;
  fileFlag = 0;
});

uploadButton.addEventListener("click", () => {
  let isFileUploaded = fileInput.value;
  if (isFileUploaded != "") {
    if (fileFlag == 0) {
      fileFlag = 1;
      var width = 0;
      var id = setInterval(frame, 50);
      function frame() {
        if (width >= 390) {
          clearInterval(id);
          uploadButton.innerHTML = `<span class="material-icons-outlined upload-button-icon"> Agregado</span> Cargado`;
        } else {
          width += 5;
          progressBar.style.width = width + "px";
        }
      }
    }
  } else {
    cannotUploadMessage.style.cssText =
      "display: flex; animation: fadeIn linear 1.5s;";
  }
});

cancelAlertButton.addEventListener("click", () => {
  cannotUploadMessage.style.cssText = "display: none;";
});

if (isAdvancedUpload) {
  [
    "drag",
    "dragstart",
    "dragend",
    "dragover",
    "dragenter",
    "dragleave",
    "drop",
  ].forEach((evt) =>
    draggableFileArea.addEventListener(evt, (e) => {
      e.preventDefault();
      e.stopPropagation();
    })
  );

  ["dragover", "dragenter"].forEach((evt) => {
    draggableFileArea.addEventListener(evt, (e) => {
      e.preventDefault();
      e.stopPropagation();
      uploadIcon.innerHTML = "Descargar";
      dragDropText.innerHTML = "Sube tu archivo!";
    });
  });

  draggableFileArea.addEventListener("drop", (e) => {
    uploadIcon.innerHTML = "Exito!";
    dragDropText.innerHTML = "Cargado exitosamente!";
    document.querySelector(
      ".label"
    ).innerHTML = `suelta o carga <span class="browse-files"> <input type="file" class="default-file-input" style=""/> <span class="browse-files-text" style="top: -23px; left: -20px;"> buscar archivo</span> </span>`;
    uploadButton.innerHTML = `Subir`;

    let files = e.dataTransfer.files;
    fileInput.files = files;
    console.log(files[0].name + " " + files[0].size);
    console.log(document.querySelector(".default-file-input").value);
    fileName.innerHTML = files[0].name;
    fileSize.innerHTML = (files[0].size / 1024).toFixed(1) + " KB";
    uploadedFile.style.cssText = "display: flex;";
    progressBar.style.width = 0;
    fileFlag = 0;
  });
}

removeFileButton.addEventListener("click", () => {
  uploadedFile.style.cssText = "display: none;";
  fileInput.value = "";
  uploadIcon.innerHTML = "Cargar";
  dragDropText.innerHTML = "sube o suelta tu archivo aquí";
  document.querySelector(
    ".label"
  ).innerHTML = `o <span class="browse-files"> <input type="file" class="default-file-input"/> <span class="browse-files-text">busca tu archivo</span> <span>desde el dispositivo</span> </span>`;
  uploadButton.innerHTML = `Subir`;
});