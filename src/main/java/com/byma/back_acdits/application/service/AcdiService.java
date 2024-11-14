package com.byma.back_acdits.application.service;

import com.byma.back_acdits.application.port.in.AcdiInPort;
import com.byma.back_acdits.application.port.out.AcdiOutPort;
import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import com.byma.back_acdits.application.util.Validador;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.exceptionHandler.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcdiService implements AcdiInPort {

    private final AcdiOutPort acdiOutPort;

    @Override
    public Acdi crearAcdi(Acdi acdi) {
        Validador.validarNoNulo(acdi);
        return acdiOutPort.guardarAcdi(acdi);
    }

    @Override
    public Acdi actualizarAcdi(Long idAcdi, Acdi acdi) {
        Validador.validarNoNulo(idAcdi, acdi);
        Acdi acdiActualizado = acdiOutPort.obtenerAcdiPorId(idAcdi)
                .orElseThrow(() -> new AcdiNoEncontradoException(ErrorMessages.ACDI_NO_ENCOTRADO));
        if (acdi.getMail() != null) {
            acdiActualizado.setMail(acdi.getMail());
        }
        if (acdi.getLiquidaEnByma() != null) {
            acdiActualizado.setLiquidaEnByma(acdi.getLiquidaEnByma());
        }
        return acdiOutPort.guardarAcdi(acdiActualizado);
    }

    @Override
    public List<Acdi> obtenerTodosLosAcdis() {
        return acdiOutPort.obtenerTodosAcdis();
    }

    @Override
    public Acdi obtenerAcdiPorId(Long idAcdi) {
        Validador.validarNoNulo(idAcdi);
        return acdiOutPort.obtenerAcdiPorId(idAcdi)
                .orElseThrow(() -> new AcdiNoEncontradoException(ErrorMessages.ACDI_NO_ENCOTRADO));
    }

    @Override
    public void eliminarAcdi(Long idAcdi) {
        Validador.validarNoNulo(idAcdi);
        if (acdiOutPort.obtenerAcdiPorId(idAcdi).isEmpty()){
            throw new AcdiNoEncontradoException(ErrorMessages.ACDI_NO_ENCOTRADO);
        }
        acdiOutPort.eliminarAcdi(idAcdi);
    }

    @Override
    public Acdi darDeBajaAcdi(Long idAcdi) {
        Validador.validarNoNulo(idAcdi);
        Acdi acdi = acdiOutPort.obtenerAcdiPorId(idAcdi)
                .orElseThrow(() -> new AcdiNoEncontradoException(ErrorMessages.ACDI_NO_ENCOTRADO));
        acdi.setHabilitado(false);
        acdi.setEstado(EstadoAcdi.DESHABILITADA);
        return acdiOutPort.guardarAcdi(acdi);
    }
}
