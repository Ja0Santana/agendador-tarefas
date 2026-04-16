package com.joaopaulo.agendador_tarefas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaopaulo.agendador_tarefas.business.TarefaService;
import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.joaopaulo.agendador_tarefas.infrastructure.security.JwtUtil;
import com.joaopaulo.agendador_tarefas.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 ao gravar tarefa")
    void deveGravarTarefaComSucesso() throws Exception {
        TarefaDTO dto = TarefaDTO.builder().nomeTarefa("Teste Controller").build();
        when(tarefaService.gravarTarefa(any(), any())).thenReturn(dto);

        mockMvc.perform(post("/tarefas")
                        .with(csrf())
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Teste Controller"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar lista de tarefas por email")
    void deveBuscarTarefasPorEmail() throws Exception {
        TarefaDTO dto = TarefaDTO.builder().nomeTarefa("Minha Tarefa").build();
        when(tarefaService.buscaListaDeTarefasPorEmail(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/tarefas")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeTarefa").value("Minha Tarefa"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 ao deletar tarefa")
    void deveDeletarTarefa() throws Exception {
        mockMvc.perform(delete("/tarefas")
                        .with(csrf())
                        .param("id", "123"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 ao atualizar status")
    void deveAtualizarStatus() throws Exception {
        TarefaDTO dto = TarefaDTO.builder().statusNotificacao(StatusNotificacao.FINALIZADA).build();
        when(tarefaService.atualizarStatusNotificacaoDaTarefa(any(), any())).thenReturn(dto);

        mockMvc.perform(patch("/tarefas")
                        .with(csrf())
                        .param("status", "FINALIZADA")
                        .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusNotificacao").value("FINALIZADA"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar tarefas por período")
    void deveBuscarPorPeriodo() throws Exception {
        TarefaDTO dto = TarefaDTO.builder().nomeTarefa("Evento").build();
        when(tarefaService.buscaListaDeTarefasPorPeriodo(any(), any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/tarefas/eventos")
                        .param("dataInicial", "2023-01-01T00:00:00")
                        .param("dataFinal", "2023-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeTarefa").value("Evento"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve alterar tarefa com sucesso")
    void deveAlterarTarefa() throws Exception {
        TarefaDTO dto = TarefaDTO.builder().nomeTarefa("Alterada").build();
        when(tarefaService.alterarTarefa(any(), eq("123"))).thenReturn(dto);

        mockMvc.perform(put("/tarefas")
                        .with(csrf())
                        .param("id", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Alterada"));
    }
}
