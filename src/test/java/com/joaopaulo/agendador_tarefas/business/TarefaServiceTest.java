package com.joaopaulo.agendador_tarefas.business;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.business.mapper.TarefaMapper;
import com.joaopaulo.agendador_tarefas.business.mapper.TarefaUpdateMapper;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.joaopaulo.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.joaopaulo.agendador_tarefas.infrastructure.repository.TarefaRepository;
import com.joaopaulo.agendador_tarefas.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaMapper tarefaMapper;

    @Mock
    private TarefaUpdateMapper tarefaUpdateMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    @DisplayName("Deve gravar tarefa com sucesso setando campos automáticos")
    void deveGravarTarefaComSucesso() {
        String token = "Bearer token-valido";
        String email = "user@email.com";
        TarefaDTO requestDTO = TarefaDTO.builder().nomeTarefa("Nova Tarefa").build();
        TarefaEntity entity = new TarefaEntity();
        
        when(jwtUtil.extractUsername(any())).thenReturn(email);
        when(tarefaMapper.paraTarefaEntity(any())).thenReturn(entity);
        when(tarefaRepository.save(any())).thenReturn(entity);
        when(tarefaMapper.paraTarefaDTO(any())).thenReturn(requestDTO);

        TarefaDTO result = tarefaService.gravarTarefa(token, requestDTO);

        assertThat(result).isNotNull();
        assertThat(requestDTO.getEmailUsuario()).isEqualTo(email);
        assertThat(requestDTO.getStatusNotificacao()).isEqualTo(StatusNotificacao.PENDENTE);
        assertThat(requestDTO.getDataCriacao()).isNotNull();
        
        verify(tarefaRepository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar tarefas por período")
    void deveBuscarTarefasPorPeriodo() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        TarefaEntity entity = new TarefaEntity();
        
        when(tarefaRepository.findByDataEventoBetweenAndStatusNotificacao(any(), any(), eq(StatusNotificacao.PENDENTE)))
                .thenReturn(List.of(entity));
        when(tarefaMapper.paraTarefaDTO(any())).thenReturn(new TarefaDTO());

        List<TarefaDTO> result = tarefaService.buscaListaDeTarefasPorPeriodo(inicio, fim);

        assertThat(result).hasSize(1);
        verify(tarefaRepository).findByDataEventoBetweenAndStatusNotificacao(inicio, fim, StatusNotificacao.PENDENTE);
    }

    @Test
    @DisplayName("Deve deletar tarefa por ID com sucesso")
    void deveDeletarTarefaComSucesso() {
        String id = "123";
        when(tarefaRepository.existsById(id)).thenReturn(true);

        tarefaService.deletarTarefaPorId(id);

        verify(tarefaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar tarefa inexistente")
    void deveLancarErroAoDeletarInexistente() {
        String id = "invalid";
        when(tarefaRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> tarefaService.deletarTarefaPorId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tarefa nao encontrada");
    }

    @Test
    @DisplayName("Deve buscar lista de tarefas por email do token")
    void deveBuscarPorEmail() {
        String token = "Bearer token-valido";
        String email = "perfil@email.com";
        TarefaEntity entity = new TarefaEntity();
        
        when(jwtUtil.extractUsername(any())).thenReturn(email);
        when(tarefaRepository.findByEmailUsuario(email)).thenReturn(List.of(entity));
        when(tarefaMapper.paraTarefaDTO(any())).thenReturn(new TarefaDTO());

        List<TarefaDTO> result = tarefaService.buscaListaDeTarefasPorEmail(token);

        assertThat(result).hasSize(1);
        verify(tarefaRepository).findByEmailUsuario(email);
    }

    @Test
    @DisplayName("Deve atualizar status de notificação com sucesso")
    void deveAtualizarStatusNotificacao() {
        String id = "1";
        StatusNotificacao novoStatus = StatusNotificacao.FINALIZADA;
        TarefaEntity entity = new TarefaEntity();
        
        when(tarefaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tarefaRepository.save(any())).thenReturn(entity);
        when(tarefaMapper.paraTarefaDTO(any())).thenReturn(new TarefaDTO());

        tarefaService.atualizarStatusNotificacaoDaTarefa(novoStatus, id);

        assertThat(entity.getStatusNotificacao()).isEqualTo(novoStatus);
        verify(tarefaRepository).save(entity);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar status de tarefa inexistente")
    void deveErroAoAtualizarStatusInexistente() {
        when(tarefaRepository.findById(any())).thenReturn(Optional.empty());
        StatusNotificacao status = StatusNotificacao.PENDENTE;

        assertThatThrownBy(() -> tarefaService.atualizarStatusNotificacaoDaTarefa(status, "999"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar erro ao alterar tarefa inexistente")
    void deveErroAoAlterarInexistente() {
        when(tarefaRepository.findById(any())).thenReturn(Optional.empty());
        TarefaDTO dto = new TarefaDTO();

        assertThatThrownBy(() -> tarefaService.alterarTarefa(dto, "999"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve alterar tarefa usando o UpdateMapper")
    void deveAlterarTarefaComSucesso() {
        String id = "1";
        TarefaDTO dto = TarefaDTO.builder().nomeTarefa("Ajustado").build();
        TarefaEntity entity = new TarefaEntity();
        
        when(tarefaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tarefaRepository.save(any())).thenReturn(entity);
        when(tarefaMapper.paraTarefaDTO(any())).thenReturn(dto);

        TarefaDTO result = tarefaService.alterarTarefa(dto, id);

        assertThat(result.getNomeTarefa()).isEqualTo("Ajustado");
        verify(tarefaUpdateMapper).updateTarefas(dto, entity);
        verify(tarefaRepository).save(entity);
    }
}
