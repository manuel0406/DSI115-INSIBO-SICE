package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;

@Service
public class AdministrativoService {

    @Autowired
    private AdministrativoRepository administrativoRepository;

    // Filtra solo a los administrativos activos
    public List<AdministrativoDTO> listarAdministrativos() {
        List<PersonalAdministrativo> administrativos = (List<PersonalAdministrativo>) administrativoRepository
                .findAll();
        return administrativos.stream()
                .filter(PersonalAdministrativo::isActivoPersonalAdministrativo)
                .sorted(Comparator.comparing(PersonalAdministrativo::getNombrePersonal)
                        .thenComparing(PersonalAdministrativo::getApellidoPersonal))
                .map(AdministrativoDTO::new)
                .collect(Collectors.toList());
    }

    // Filtra solo a los administrativos inactivos
    public List<AdministrativoDTO> listarAdministrativosInactivos() {
        List<PersonalAdministrativo> administrativos = (List<PersonalAdministrativo>) administrativoRepository
                .findAll();
        return administrativos.stream()
                .filter(admin -> !admin.isActivoPersonalAdministrativo())
                .sorted(Comparator.comparing(PersonalAdministrativo::getNombrePersonal)
                        .thenComparing(PersonalAdministrativo::getApellidoPersonal))
                .map(AdministrativoDTO::new)
                .collect(Collectors.toList());
    }

    public boolean guardarAdministrativo(PersonalAdministrativo administrativo) {
        // Verificar si el docente ya existe en la base de datos
        boolean existe = administrativoRepository.existsById(administrativo.getDuiPersonal());

        // Guardar el docente
        administrativoRepository.save(administrativo);

        // Devolver true si se creó un nuevo expediente, false si se actualizó uno existente
        return !existe;
    }

    public PersonalAdministrativo buscarPorIdAdministrativo(String duiPersonal) {
        return administrativoRepository.findById(duiPersonal).orElse(null);
    }

    public List<PersonalAdministrativo> personal(){
        return (List<PersonalAdministrativo>) administrativoRepository.findAll();
    }

    public void eliminar(String duiPersonal) {
        administrativoRepository.deleteById(duiPersonal);
    }

    public boolean correoYaRegistrado(String correoPersonal) {
        return administrativoRepository.existsByCorreoPersonal(correoPersonal);
    }

    public boolean nitYaRegistrado(String nitPersonal) {
        return administrativoRepository.existsBynitPersonal(nitPersonal);
    }

    public boolean nupYaRegistrado(String nupPersonal) {
        return administrativoRepository.existsBynupPersonal(nupPersonal);
    }
}
