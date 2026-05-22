package com.joaopaulo.agendador_tarefas.infrastructure.security;

import com.joaopaulo.agendador_tarefas.business.dto.UsuarioDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl{

    private final UsuarioClient usuarioClient;

    public UserDetails carregarDadosUsuario(String email, String token) {
        UsuarioDTO usuarioDTO = usuarioClient.buscarUsuarioPorEmail(email, token);
        if (usuarioDTO == null) {
            return null;
        }
        return User
                .withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : "")
                .build();
    }

}
