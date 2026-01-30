package com.joaopaulo.agendador_tarefas.business.mapper;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefaMapper {
    TarefaDTO paraTarefaDTO(TarefaEntity tarefa);

    TarefaEntity paraTarefaEntity(TarefaDTO tarefa);
}
