package com.helptask.service;

import com.helptask.entity.ChangeStatus;
import com.helptask.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface TaskService {
    Task createOrUpdate(Task task);
    Optional<Task> findById(String id);
    void delete(String id);
    Page<Task> listTasks(int page, int count);
    ChangeStatus createChangesStatus(ChangeStatus changeStatus);
    Iterable<ChangeStatus> listChangeStatus(String idTicket);
    Page<Task> findByCurrentUser(int page, int count, String idUsuario);
    Page<Task> findByParameters(int page, int count, String titulo, String status, String priority);
    Page<Task> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String priority, String idUsuario);
    Page<Task> findByNumber(int page, int count, Integer number);
    Iterable<Task> findAll();
    Page<Task> findByParametersAndAssignedUser(int page, int count, String titulo, String status, String priority, String assignedUser);
    Integer generatedNumber();
}
