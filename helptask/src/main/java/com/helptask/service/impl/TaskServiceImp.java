package com.helptask.service.impl;

import com.helptask.entity.ChangeStatus;
import com.helptask.entity.Task;
import com.helptask.repository.ChangeStatusRepository;
import com.helptask.repository.TaskRepository;
import com.helptask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ChangeStatusRepository changeStatusRepository;

    @Override
    public Task createOrUpdate(Task task) {
        return taskRepository.save(task);
    }

    public Integer generatedNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Page<Task> listTasks(int page, int count) {
        Pageable pageable = PageRequest.of(page,count);
        return taskRepository.findAll(pageable);
    }

    @Override
    public ChangeStatus createCahngesStatus(ChangeStatus changeStatus) {
        return changeStatusRepository.save(changeStatus);
    }

    @Override
    public Iterable<ChangeStatus> listChangeStatus(String idTicket) {
        return changeStatusRepository.findByTicketIdOrderByDataDesc(idTicket);
    }

    @Override
    public Page<Task> findByCurrentUser(int page, int count, String idUsuario) {
        Pageable pageable= PageRequest.of(page,count);
        return taskRepository.findByUsuarioIdOrderByDataDesc(idUsuario, pageable);
    }

    @Override
    public Page<Task> findByParameters(int page, int count, String titulo, String status, String priority) {
        Pageable pageable = PageRequest.of(page,count);
        return taskRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingOrderByDataDesc(titulo, status, priority, pageable);
    }

    @Override
    public Page<Task> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String priority, String idUsuario) {
        Pageable pageable = PageRequest.of(page,count);
        return taskRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndUsuarioIdOrderByDataDesc(titulo, status, priority, pageable, idUsuario);
    }

    @Override
    public Page<Task> findByNumber(int page, int count, Integer number) {
        Pageable pageable = PageRequest.of(page,count);
        return taskRepository.findByNumber(number,pageable);
    }

    @Override
    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> findByParametersAndAssignedUser(int page, int count, String titulo, String status, String priority, String assignedUser) {
        Pageable pageable = PageRequest.of(page,count);
        return taskRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndAssigneredUserIdOrderByDataDesc(titulo, status, priority, pageable,assignedUser);
    }
}
