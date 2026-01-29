package com.joaopaulo.agendador_tarefas.infrastructure.security;

import com.joaopaulo.agendador_tarefas.business.dto.UsuarioDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl{

    private UsuarioClient usuarioClient;

    public UserDetails carregarDadosUsuario(String email, String token) {
        UsuarioDTO usuarioDTO = usuarioClient.buscarUsuarioPorEmail(email, token);
        return User
                .withUsername(usuarioDTO.getEmail()) // Define o nome de usuário como o e-mail
                .password(usuarioDTO.getSenha()) // Define a senha do usuário
                .build();
    }

}
