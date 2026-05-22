package com.joaopaulo.agendador_tarefas.business;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.business.mapper.TarefaMapper;
import com.joaopaulo.agendador_tarefas.business.mapper.TarefaUpdateMapper;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.joaopaulo.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.joaopaulo.agendador_tarefas.infrastructure.repository.TarefaRepository;
import com.joaopaulo.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;
    private final TarefaUpdateMapper tarefaUpdateMapper;
    private final JwtUtil jwtUtil;

    public TarefaDTO gravarTarefa(String token, TarefaDTO tarefaDTO) {
        validarDataEvento(tarefaDTO.getDataEvento());
        String emailUsuario = jwtUtil.extractUsername(token.substring(7));
        tarefaDTO.setEmailUsuario(emailUsuario);
        tarefaDTO.setDataCriacao(LocalDateTime.now());
        tarefaDTO.setStatusNotificacao(StatusNotificacao.PENDENTE);
        TarefaEntity tarefaEntity = tarefaMapper.paraTarefaEntity(tarefaDTO);
        return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
    }

    public List<TarefaDTO> buscaListaDeTarefasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        List<TarefaEntity> tarefas = tarefaRepository.findByDataEventoBetweenAndStatusNotificacao(dataInicial, dataFinal, StatusNotificacao.PENDENTE);
        return tarefas.stream()
                .map(tarefaMapper::paraTarefaDTO)
                .toList();
    }

    public List<TarefaDTO> buscaListaDeTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefaEntity> tarefas = tarefaRepository.findByEmailUsuario(email);

        LocalDateTime now = LocalDateTime.now();
        boolean isUpdated = false;

        for (TarefaEntity tarefa : tarefas) {
            if (tarefa.getDataEvento() != null && tarefa.getDataEvento().isBefore(now)) {
                if (tarefa.getStatusNotificacao() == StatusNotificacao.PENDENTE || 
                    tarefa.getStatusNotificacao() == StatusNotificacao.NOTIFICADA) {
                    tarefa.setStatusNotificacao(StatusNotificacao.VENCIDA);
                    isUpdated = true;
                }
            }
        }

        if (isUpdated) {
            tarefaRepository.saveAll(tarefas);
        }

        return tarefas.stream()
                .map(tarefaMapper::paraTarefaDTO)
                .toList();
    }
    public void deletarTarefaPorId(String id) {
        if (!tarefaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa nao encontrada para id: " + id);
        }
        tarefaRepository.deleteById(id);
    }

    public TarefaDTO atualizarStatusNotificacaoDaTarefa(StatusNotificacao statusNotificacao, String id) {
        TarefaEntity tarefaEntity = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada para id: " + id));
        tarefaEntity.setStatusNotificacao(statusNotificacao);
        return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
    }

    public TarefaDTO alterarTarefa(TarefaDTO tarefaDTO, String id) {
        validarDataEvento(tarefaDTO.getDataEvento());
        TarefaEntity tarefaEntity = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada para id: " + id));
        tarefaUpdateMapper.updateTarefas(tarefaDTO, tarefaEntity);
        
        // Se a data do evento da tarefa for no futuro, ela deve retornar ao status PENDENTE
        if (tarefaEntity.getDataEvento() != null && tarefaEntity.getDataEvento().isAfter(LocalDateTime.now(java.time.ZoneId.of("America/Sao_Paulo")))) {
            tarefaEntity.setStatusNotificacao(StatusNotificacao.PENDENTE);
        }
        
        return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
    }

    private void validarDataEvento(LocalDateTime dataEvento) {
        if (dataEvento != null) {
            LocalDateTime minPermitted = LocalDateTime.now(java.time.ZoneId.of("America/Sao_Paulo")).minusMinutes(1);
            if (dataEvento.isBefore(minPermitted)) {
                throw new IllegalArgumentException("A data do evento não pode estar no passado.");
            }
        }
    }
}
