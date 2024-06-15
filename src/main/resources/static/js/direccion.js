// data.js
const data = {
    "departamentos": {
      "Ahuachapán": {
        "municipios": {
          "Ahuachapán Norte": ["Atiquizaya", "El Refugio", "San Lorenzo", "Turín"],
          "Ahuachapán Centro": ["Ahuachapán", "Apaneca", "Concepción de Ataco", "Tacuba"],
          "Ahuachapán Sur": ["Guaymango", "Jujutla", "San Francisco Menéndez", "San Pedro Puxtla"]
        }
      },
      "San Salvador": {
        "municipios": {
          "San Salvador Norte": ["Aguilares", "El Paisnal", "Guazapa"],
          "San Salvador Oeste": ["Apopa", "Nejapa"],
          "San Salvador Este": ["Ilopango", "San Martín", "Soyapango", "Tonacatepeque"],
          "San Salvador Centro": ["Ayutuxtepeque", "Mejicanos", "San Salvador", "Cuscatancingo", "Ciudad Delgado"],
          "San Salvador Sur": ["Panchimalco", "Rosario de Mora", "San Marcos", "Santo Tomás", "Santiago Texacuangos"]
        }
      },
      "La Libertad": {
        "municipios": {
          "La Libertad Norte": ["Quezaltepeque", "San Matías", "San Pablo Tacachico"],
          "La Libertad Centro": ["San Juan Opico", "Ciudad Arce"],
          "La Libertad Oeste": ["Colón", "Jayaque", "Sacacoyo", "Tepecoyo", "Talnique"],
          "La Libertad Este": ["Antiguo Cuscatlán", "Huizúcar", "Nuevo Cuscatlán", "San José Villanueva", "Zaragoza"],
          "La Libertad Costa": ["Chiltiupán", "Jicalapa", "La Libertad", "Tamanique", "Teotepeque"],
          "La Libertad Sur": ["Comasagua", "Santa Tecla"]
        }
      },
      "Chalatenango": {
        "municipios": {
          "Chalatenango Norte": ["La Palma", "Citalá", "San Ignacio"],
          "Chalatenango Centro": ["Nueva Concepción", "Tejutla", "La Reina", "Agua Caliente", "Dulce Nombre de María", "El Paraíso", "San Fernando", "San Francisco Morazán", "San Rafael", "Santa Rita"],
          "Chalatenango Sur": ["Chalatenango", "Arcatao", "Azacualpa", "Comalapa", "Concepción Quezaltepeque", "El Carrizal", "La Laguna", "Las Vueltas", "Nombre de Jesús", "Nueva Trinidad", "Ojos de Agua", "Potonico", "San Antonio de La Cruz", "San Antonio Los Ranchos", "San Francisco Lempa", "San Isidro Labrador", "San José Cancasque", "San Miguel de Mercedes", "San José Las Flores", "San Luis del Carmen"]
        }
      },
      "Cuscatlán": {
        "municipios": {
          "Cuscatlán Norte": ["Suchitoto", "San José Guayabal", "Oratorio de Concepción", "San Bartolomé Perulapía", "San Pedro Perulapán"],
          "Cuscatlán Sur": ["Cojutepeque", "San Rafael Cedros", "Candelaria", "Monte San Juan", "El Carmen", "San Cristobal", "Santa Cruz Michapa", "San Ramón", "El Rosario", "Santa Cruz Analquito", "Tenancingo"]
        }
      },
      "Cabañas": {
        "municipios": {
          "Cabañas Este": ["Sensuntepeque", "Victoria", "Dolores", "Guacotecti", "San Isidro"],
          "Cabañas Oeste": ["Ilobasco", "Tejutepeque", "Jutiapa", "Cinquera"]
        }
      },
      "La Paz": {
        "municipios": {
          "La Paz Oeste": ["Cuyultitán", "Olocuilta", "San Juan Talpa", "San Luis Talpa", "San Pedro Masahuat", "Tapalhuaca", "San Francisco Chinameca"],
          "La Paz Centro": ["El Rosario", "Jerusalén", "Mercedes La Ceiba", "Paraíso de Osorio", "San Antonio Masahuat", "San Emigdio", "San Juan Tepezontes", "San Luis La Herradura", "San Miguel Tepezontes", "San Pedro Nonualco", "Santa María Ostuma", "Santiago Nonualco"],
          "La Paz Este": ["San Juan Nonualco", "San Rafael Obrajuelo", "Zacatecoluca"]
        }
      },
      "La Unión": {
        "municipios": {
          "La Unión Norte": ["Anamorós", "Bolivar", "Concepción de Oriente", "El Sauce", "Lislique", "Nueva Esparta", "Pasaquina", "Polorós", "San José La Fuente", "Santa Rosa de Lima"],
          "La Unión Sur": ["Conchagua", "El Carmen", "Intipucá", "La Unión", "Meanguera del Golfo", "San Alejo", "Yayantique", "Yucuaiquín"]
        }
      },
      "Usulután": {
        "municipios": {
          "Usulután Norte": ["Santiago de María", "Alegría", "Berlín", "Mercedes Umaña", "Jucuapa", "El Triunfo", "Estanzuelas", "San Buenaventura", "Nueva Granada"],
          "Usulután Este": ["Usulután", "Jucuarán", "San Dionisio", "Concepción Batres", "Santa María", "Ozatlán", "Tecapán", "Santa Elena", "California", "Ereguayquín"],
          "Usulután Oeste": ["Jiquilisco", "Puerto El Triunfo", "San Agustín", "San Francisco Javier"]
        }
      },
      "Sonsonate": {
        "municipios": {
          "Sonsonate Norte": ["Juayúa", "Nahuizalco", "Salcoatitán", "Santa Catarina Masahuat"],
          "Sonsonate Centro": ["Sonsonate", "Sonzacate", "Nahulingo", "San Antonio del Monte", "Santo Domingo de Guzmán"],
          "Sonsonate Este": ["Izalco", "Armenia", "Caluco", "San Julián", "Cuisnahuat", "Santa Isabel Ishuatán"],
          "Sonsonate Oeste": ["Acajutla"]
        }
      },
      "Santa Ana": {
        "municipios": {
          "Santa Ana Norte": ["Masahuat", "Metapán", "Santa Rosa Guachipilín", "Texistepeque"],
          "Santa Ana Centro": ["Santa Ana"],
          "Santa Ana Este": ["Coatepeque", "El Congo"],
          "Santa Ana Oeste": ["Candelaria de la Frontera", "Chalchuapa", "El Porvenir", "San Antonio Pajonal", "San Sebastián Salitrillo", "Santiago de La Frontera"]
        }
      },
      "San Vicente": {
        "municipios": {
          "San Vicente Norte": ["Apastepeque", "Santa Clara", "San Ildefonso", "San Esteban Catarina", "San Sebastián", "San Lorenzo", "Santo Domingo"],
          "San Vicente Sur": ["San Vicente", "Guadalupe", "Verapaz", "Tepetitán", "Tecoluca", "San Cayetano Istepeque"]
        }
      },
      "San Miguel": {
        "municipios": {
          "San Miguel Norte": ["Ciudad Barrios", "Sesori", "Nuevo Edén de San Juan", "San Gerardo", "San Luis de La Reina", "Carolina", "San Antonio del Mosco", "Chapeltique"],
          "San Miguel Centro": ["San Miguel", "Comacarán", "Uluazapa", "Moncagua", "Quelepa", "Chirilagua"],
          "San Miguel Oeste": ["Chinameca", "Nueva Guadalupe", "Lolotique", "San Jorge", "San Rafael Oriente", "El Tránsito"]
        }
      },
      "Morazán": {
        "municipios": {
          "Morazán Norte": ["Arambala", "Cacaopera", "Corinto", "El Rosario", "Joateca", "Jocoaitique", "Meanguera", "Perquín", "San Fernando", "San Isidro", "Torola"],
          "Morazán Sur": ["Chilanga", "Delicias de Concepción", "El Divisadero", "Gualococti", "Guatajiagua", "Jocoro", "Lolotiquillo", "Osicala", "San Carlos", "San Francisco Gotera", "San Simón", "Sensembra", "Sociedad", "Yamabal", "Yoloaiquín"]
        }
      }
    }
  };
  
  // script.js
  document.addEventListener("DOMContentLoaded", function() {
    const departamentoSelect = document.getElementById("docente_departamento");
    const municipioSelect = document.getElementById("docente_municipio");
    const distritoSelect = document.getElementById("docente_distrito");

    function populateSelect(select, options, selectedValue) {
        select.innerHTML = "<option value='' selected>Seleccionar...</option>"; // Agrega value='' y selected a la primera opción
        options.forEach(option => {
            const optionElement = document.createElement("option");
            optionElement.value = option;
            optionElement.textContent = option;
            if (option === selectedValue) {
                optionElement.selected = true;
            }
            select.appendChild(optionElement);
        });
    }

    function onDepartamentoChange() {
        const selectedDepartamento = departamentoSelect.value;
        const municipios = selectedDepartamento !== '' ? Object.keys(data.departamentos[selectedDepartamento].municipios) : [];
        populateSelect(municipioSelect, municipios, municipioSelect.getAttribute('data-selected'));
        populateSelect(distritoSelect, [], distritoSelect.getAttribute('data-selected')); // Limpiamos los distritos cuando cambia el departamento
    }

    function onMunicipioChange() {
        const selectedDepartamento = departamentoSelect.value;
        const selectedMunicipio = municipioSelect.value;
        const distritos = selectedDepartamento !== '' && selectedMunicipio !== '' ? data.departamentos[selectedDepartamento].municipios[selectedMunicipio] : [];
        populateSelect(distritoSelect, distritos, distritoSelect.getAttribute('data-selected'));
    }

    const departamentos = Object.keys(data.departamentos);
    populateSelect(departamentoSelect, departamentos, departamentoSelect.getAttribute('data-selected'));

    departamentoSelect.addEventListener("change", onDepartamentoChange);
    municipioSelect.addEventListener("change", onMunicipioChange);

    // Inicializa los selectores si ya tienen valores seleccionados
    const initialDepartamento = departamentoSelect.getAttribute('data-selected');
    const initialMunicipio = municipioSelect.getAttribute('data-selected');
    const initialDistrito = distritoSelect.getAttribute('data-selected');

    if (initialDepartamento) {
        departamentoSelect.value = initialDepartamento;
        onDepartamentoChange();
    }

    if (initialMunicipio) {
        municipioSelect.value = initialMunicipio;
        onMunicipioChange();
    }

    if (initialDistrito) {
        distritoSelect.value = initialDistrito;
    }
});
