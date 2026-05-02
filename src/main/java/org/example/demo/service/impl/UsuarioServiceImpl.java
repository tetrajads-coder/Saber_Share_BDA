package org.example.demo.service.impl;

import lombok.AllArgsConstructor;
import org.example.demo.model.Usuario;
import org.example.demo.repository.UsuarioRepository;
import org.example.demo.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario update(Integer id, Usuario usuario) {
        Usuario aux = usuarioRepository.findById(id).orElse(null);
        if (aux == null) {
            return null;
        }

        aux.setUsuUsu(usuario.getUsuUsu());
        aux.setNomUsu(usuario.getNomUsu());
        aux.setApeUsu(usuario.getApeUsu());
        aux.setCorreoUsu(usuario.getCorreoUsu());
        // Solo actualizar contraseña si viene un valor no vacío
        if (usuario.getContraUsu() != null && !usuario.getContraUsu().isBlank()) {
            aux.setContraUsu(passwordEncoder.encode(usuario.getContraUsu()));
        }
        aux.setFotUsu(usuario.getFotUsu());
        aux.setInteUsu(usuario.getInteUsu());
        aux.setTelUsu(usuario.getTelUsu());

        return usuarioRepository.save(aux);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreoUsu(correo).orElse(null);
    }
}
