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
        String emailUsuario = jwtUtil.extractUsername(token.substring(7));
        tarefaDTO.setEmailUsuario(emailUsuario);
        tarefaDTO.setDataCriacao(LocalDateTime.now());
        tarefaDTO.setStatusNotificacao(StatusNotificacao.PENDENTE);
        TarefaEntity tarefaEntity = tarefaMapper.paraTarefaEntity(tarefaDTO);
        return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
    }

    public List<TarefaDTO> buscarTarefasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        List<TarefaEntity> tarefas = tarefaRepository.findByDataEventoBetween(dataInicial, dataFinal);
        return tarefas.stream()
                .map(tarefaMapper::paraTarefaDTO)
                .toList();
    }

    public List<TarefaDTO> buscarTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefaEntity> tarefas = tarefaRepository.findByEmailUsuario(email);
        return tarefas.stream()
                .map(tarefaMapper::paraTarefaDTO)
                .toList();
    }
    public void deletarTarefaPorId(String id) {
        try{
        tarefaRepository.deleteById(id);
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente: " + id + e.getCause());
        }
    }
    public TarefaDTO alterarStatusDaTarefaPorId(StatusNotificacao statusNotificacao, String id) {
        try {
            TarefaEntity tarefaEntity = tarefaRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada para o id: " + id));

        tarefaEntity.setStatusNotificacao(statusNotificacao);
        return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Tarefa nao encontrada para o id: " + id + e.getCause());
        }
    }

    public TarefaDTO alterarTarefa (TarefaDTO tarefaDTO, String id) {
        try {
            TarefaEntity tarefaEntity = tarefaRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada para o id: " + id));
            tarefaUpdateMapper.updateTarefas(tarefaDTO, tarefaEntity);
            return tarefaMapper.paraTarefaDTO(tarefaRepository.save(tarefaEntity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Tarefa nao encontrada para o id: " + id + e.getCause());
        }
    }
}
