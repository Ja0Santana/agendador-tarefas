package com.joaopaulo.agendador_tarefas.business.mapper;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TarefaUpdateMapperTest {

    private final TarefaUpdateMapper mapper = Mappers.getMapper(TarefaUpdateMapper.class);

    @Test
    @DisplayName("Deve atualizar apenas campos não nulos da Entity")
    void deveAtualizarApenasCamposNaoNulos() {
        TarefaEntity entity = TarefaEntity.builder()
                .nomeTarefa("Original")
                .descricaoTarefa("Desc Original")
                .build();

        TarefaDTO dto = TarefaDTO.builder()
                .nomeTarefa("Alterado")
                .descricaoTarefa(null) // Deve ser ignorado pelo mapper
                .build();

        mapper.updateTarefas(dto, entity);

        assertThat(entity.getNomeTarefa()).isEqualTo("Alterado");
        assertThat(entity.getDescricaoTarefa()).isEqualTo("Desc Original");
    }
}
