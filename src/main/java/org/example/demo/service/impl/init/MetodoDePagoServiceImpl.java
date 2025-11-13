package org.example.demo.service.impl.init;

import lombok.AllArgsConstructor;
import org.example.demo.model.MetodoDePago;
import org.example.demo.repository.MetodoDePagoRepository;
import org.example.demo.service.MetodoDePagoService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MetodoDePagoServiceImpl implements MetodoDePagoService {

    private final MetodoDePagoRepository repo;

    @Override
    public List<MetodoDePago> getAll() {
        return repo.findAll();
    }

    @Override
    public MetodoDePago getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public MetodoDePago save(MetodoDePago m) {
        return repo.save(m);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public MetodoDePago update(Integer id, MetodoDePago m) {
        MetodoDePago x = repo.findById(id).orElse(null);
        if (x == null) return null;

        x.setCompania(m.getCompania());
        x.setNumtar(m.getNumtar());
        x.setCvv(m.getCvv());
        x.setVenci(m.getVenci());
        x.setUsuario(m.getUsuario());

        return repo.save(x);
    }
}

