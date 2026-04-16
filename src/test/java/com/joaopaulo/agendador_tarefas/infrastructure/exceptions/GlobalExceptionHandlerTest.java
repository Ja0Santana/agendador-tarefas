package com.joaopaulo.agendador_tarefas.infrastructure.exceptions;

import com.joaopaulo.agendador_tarefas.business.TarefaService;
import com.joaopaulo.agendador_tarefas.controller.TarefaController;
import com.joaopaulo.agendador_tarefas.infrastructure.security.JwtUtil;
import com.joaopaulo.agendador_tarefas.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TarefaController.class)
@org.springframework.context.annotation.Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 404 quando ResourceNotFoundException for lançada")
    void deveRetornar404NoResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Tarefa não encontrada"))
                .when(tarefaService).deletarTarefaPorId(anyString());

        mockMvc.perform(delete("/tarefas")
                        .with(csrf())
                        .queryParam("id", "invalid-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tarefa não encontrada"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 401 quando UnauthorizedException for lançada")
    void deveRetornar401NoUnauthorized() throws Exception {
        doThrow(new UnauthorizedException("Não autorizado"))
                .when(tarefaService).buscaListaDeTarefasPorEmail(anyString());

        mockMvc.perform(get("/tarefas")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isUnauthorized());
    }
}
