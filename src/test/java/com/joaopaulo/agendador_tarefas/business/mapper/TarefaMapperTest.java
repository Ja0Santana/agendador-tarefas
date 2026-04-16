package com.joaopaulo.agendador_tarefas.business.mapper;

import com.joaopaulo.agendador_tarefas.business.dto.TarefaDTO;
import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TarefaMapperTest {

    private final TarefaMapper mapper = Mappers.getMapper(TarefaMapper.class);

    @Test
    @DisplayName("Deve converter Entity para DTO corretamente")
    void deveConverterEntityParaDTO() {
        LocalDateTime agora = LocalDateTime.now();
        TarefaEntity entity = TarefaEntity.builder()
                .id("1")
                .nomeTarefa("Teste")
                .descricaoTarefa("Desc")
                .dataEvento(agora)
                .emailUsuario("teste@email.com")
                .statusNotificacao(StatusNotificacao.PENDENTE)
                .build();

        TarefaDTO dto = mapper.paraTarefaDTO(entity);

        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getNomeTarefa()).isEqualTo(entity.getNomeTarefa());
        assertThat(dto.getDataEvento()).isEqualTo(entity.getDataEvento());
        assertThat(dto.getStatusNotificacao()).isEqualTo(entity.getStatusNotificacao());
        assertThat(dto.getEmailUsuario()).isEqualTo(entity.getEmailUsuario());
    }

    @Test
    @DisplayName("Deve converter DTO para Entity corretamente")
    void deveConverterDTOParaEntity() {
        TarefaDTO dto = TarefaDTO.builder()
                .nomeTarefa("DTO Tarefa")
                .emailUsuario("user@email.com")
                .statusNotificacao(StatusNotificacao.FINALIZADA)
                .build();

        TarefaEntity entity = mapper.paraTarefaEntity(dto);

        assertThat(entity.getNomeTarefa()).isEqualTo(dto.getNomeTarefa());
        assertThat(entity.getEmailUsuario()).isEqualTo(dto.getEmailUsuario());
        assertThat(entity.getStatusNotificacao()).isEqualTo(dto.getStatusNotificacao());
    }
}
