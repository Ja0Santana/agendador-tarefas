package com.joaopaulo.agendador_tarefas.infrastructure.repository;

import com.joaopaulo.agendador_tarefas.infrastructure.entity.TarefaEntity;
import com.joaopaulo.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefaRepository extends MongoRepository<TarefaEntity, String> {

    List<TarefaEntity> findByDataEventoBetweenAndStatusNotificacao(LocalDateTime dataInicial, LocalDateTime dataFinal,
                                                                   StatusNotificacao statusNotificacao);
    List<TarefaEntity> findByEmailUsuario(String emailUsuario);
}
