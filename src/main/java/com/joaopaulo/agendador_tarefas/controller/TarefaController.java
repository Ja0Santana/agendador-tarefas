package com.joaopaulo.agendador_tarefas.controller;

import com.joaopaulo.agendador_tarefas.business.TarefaService;
import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.joaopaulo.agendador_tarefas.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefa", description = "Endpoints para criação e gerenciamento de tarefas")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class TarefaController {
    private final TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa", description = "Endpoint para criar uma nova tarefa.")
    @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TarefaDTO> gravarTarefa(@RequestBody TarefaDTO tarefa, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefaService.gravarTarefa(token, tarefa));
    }

    @GetMapping("/eventos")
    @Operation(summary = "Busca uma lista de tarefas dentro de um período", description = "Endpoint para listar todas as tarefas cadastradas em que a data final está em um certo período.")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<TarefaDTO>> buscaListaDeTarefasPorPeriodo(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
        return ResponseEntity.ok(tarefaService.buscaListaDeTarefasPorPeriodo(dataInicial, dataFinal));
    }

    @GetMapping
    @Operation(summary = "Busca uma lista de tarefas por email de usuário", description = "Endpoint para listar todas as tarefas cadastradas por um certo usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<TarefaDTO>> buscaListaDeTarefasPorEmail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefaService.buscaListaDeTarefasPorEmail(token));
    }

    @DeleteMapping
    @Operation(summary = "Deleta tarefas por ID", description = "Endpoint para listar todas as tarefas cadastradas em que a data final está em um certo período.")
    @ApiResponse(responseCode = "200", description = "Tarefa deletada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletarTarefaPorId(@RequestParam("id") String id) {
        tarefaService.deletarTarefaPorId(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Operation(summary = "Atualiza o status de uma tarefa por ID", description = "Endpoint para atualizar o status de notificação de uma tarefa por ID.")
    @ApiResponse(responseCode = "200", description = "Status da tarefa atualizado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TarefaDTO> atualizarStatusNotificacaoDaTarefa(@RequestParam("status")StatusNotificacao statusNotificacao, @RequestParam("id") String id) {
        return ResponseEntity.ok(tarefaService.atualizarStatusNotificacaoDaTarefa(statusNotificacao, id));
    }

    @PutMapping
    @Operation(summary = "Atualiza os dados de uma tarefa por ID", description = "Endpoint para atualizar os dados de uma tarefa por ID.")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TarefaDTO> alterarTarefa(@RequestBody TarefaDTO tarefaDTO, @RequestParam("id") String id) {
        return ResponseEntity.ok(tarefaService.alterarTarefa(tarefaDTO, id));
    }
}
