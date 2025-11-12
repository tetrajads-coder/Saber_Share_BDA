package org.example.demo.dto;

import lombok.*;
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UsuarioDto {
    private Integer id;
    private String user;
    private String nombre;
    private String apellido;
    private String password;
    private String correo;
    private String telefono;
}
