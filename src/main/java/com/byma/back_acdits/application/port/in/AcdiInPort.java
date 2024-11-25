package com.byma.back_acdits.application.port.in;

import com.byma.back_acdits.domain.model.Acdi;
import java.util.List;

public interface AcdiInPort {
    Acdi crearAcdi(Acdi acdi);
    Acdi actualizarAcdi(Long idAcdi, Acdi acdi);
    List<Acdi> obtenerTodosLosAcdis();
    Acdi obtenerAcdiPorId(Long idAcdi);
    void eliminarAcdi(Long idAcdi);
    Acdi darDeBajaAcdi(Long idAcdi);
}
