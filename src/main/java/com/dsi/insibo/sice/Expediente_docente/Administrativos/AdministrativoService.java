package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;

@Service
public class AdministrativoService {

    @Autowired
    private AdministrativoRepository administrativoRepository;

    public List<AdministrativoDTO> listarAdministrativos() {
        List<PersonalAdministrativo> administrativos = (List<PersonalAdministrativo>) administrativoRepository
                .findAll();
        return administrativos.stream()
                .map(AdministrativoDTO::new)
                .collect(Collectors.toList());
    }

    public boolean guardarAdministrativo(PersonalAdministrativo administrativo) {
        // Verificar si el docente ya existe en la base de datos
        boolean existe = administrativoRepository.existsById(administrativo.getDuiPersonal());

        // Guardar el docente
        administrativoRepository.save(administrativo);

        // Devolver true si se creó un nuevo expediente, false si se actualizó uno
        // existente
        return !existe;
    }

    public PersonalAdministrativo buscarPorIdAdministrativo(String duiPersonal) {
        return administrativoRepository.findById(duiPersonal).orElse(null);
    }

    public void eliminar(String duiPersonal) {
        administrativoRepository.deleteById(duiPersonal);
    }
}
