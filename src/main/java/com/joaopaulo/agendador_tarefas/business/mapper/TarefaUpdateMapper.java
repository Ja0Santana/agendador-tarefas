package com.joaopaulo.agendador_tarefas.business.mapper;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaUpdateMapper {
    void updateTarefas(TarefaDTO tarefaDTO, @MappingTarget TarefaEntity tarefaEntity);
}
