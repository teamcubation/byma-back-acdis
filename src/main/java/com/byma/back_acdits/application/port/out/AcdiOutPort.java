package com.byma.back_acdits.application.port.out;

import com.byma.back_acdits.domain.model.Acdi;

import java.util.List;
import java.util.Optional;

public interface AcdiOutPort {
    Acdi guardarAcdi(Acdi acdi);
    Optional<Acdi> obtenerAcdiPorId(Long idAcdi);
    List<Acdi> obtenerTodosAcdis();
    void eliminarAcdi(Long idAcdi);
}
