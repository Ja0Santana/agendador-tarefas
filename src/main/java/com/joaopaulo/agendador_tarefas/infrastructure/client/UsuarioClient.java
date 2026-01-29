package com.joaopaulo.agendador_tarefas.infrastructure.client;

import com.joaopaulo.agendador_tarefas.business.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    UsuarioDTO buscarUsuarioPorEmail(@RequestParam("email") String email,
                                     @RequestHeader("Authorization") String token);
}
