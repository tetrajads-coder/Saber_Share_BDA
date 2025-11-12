package org.example.demo.service.impl;

import org.example.demo.model.Servicio;
import org.example.demo.repository.ServicioRepository;
import org.example.demo.service.ServicioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ServicioServiceImpl implements ServicioService
{
    private final ServicioRepository servicioRepository;

    @Override
    public List<Servicio> getAll()
    {
        return servicioRepository.findAll();
    }

    @Override
    public Servicio getById(Integer id)
    {
        return servicioRepository.findById(id).orElse(null);
    }

    @Override
    public Servicio save(Servicio servicio)
    {
        return servicioRepository.save(servicio);
    }

    @Override
    public void delete(Integer id)
    {
        servicioRepository.deleteById(id);
    }

    @Override
    public Servicio update(Integer id, Servicio servicio)
    {
        Servicio aux = servicioRepository.getById(id);
        aux.setTitSer(servicio.getTitSer());
        aux.setDescripcion(servicio.getDescripcion());
        aux.setPrecioSer(servicio.getPrecioSer());
        aux.setReqSer(servicio.getReqSer());
        aux.setFechaSer(servicio.getFechaSer());
        aux.setHora(servicio.getHora());
        aux.setUsuario(servicio.getUsuario());
        servicioRepository.save(aux);
        return aux;
    }
}
