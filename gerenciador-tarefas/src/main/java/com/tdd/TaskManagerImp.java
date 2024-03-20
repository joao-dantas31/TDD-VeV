package com.tdd;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskManagerImp implements TaskManager {

    private final List<Task> queue;

    private final AtomicLong idGenerator;

    public TaskManagerImp() {
        queue = new ArrayList<>();
        idGenerator = new AtomicLong(1);
    }

    @Override
    public Task create(String title, String description, Date dueDate, Priority priority) {
        if (title == null) {
            throw new NullPointerException("Title cannot be null");
        }

        if (description == null) {
            throw new NullPointerException("Description cannot be null");
        }

        if (dueDate == null) {
            throw new NullPointerException("Due Date cannot be null");
        }

        if (priority == null) {
            throw new NullPointerException("com.tdd.Priority cannot be null");
        }

        Task newTask = new Task(idGenerator.getAndIncrement(), title, description, dueDate, priority);

        queue.add(newTask);

        return newTask.toBuilder().build();
    }

    @Override
    public Task update(Long id, @Nullable String title, @Nullable String description, @Nullable Date dueDate, @Nullable Priority priority) {
        Task found = queue.stream().filter(task -> task.getId().equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException("Cannot find task with id: " + id));

        Task updated = Task.builder().id(id).title(title != null ? title : found.getTitle()).description(description != null ? description : found.getDescription()).dueDate(dueDate != null ? dueDate : found.getDueDate()).priority(priority != null ? priority : found.getPriority()).build();

        queue.remove(found);
        queue.add(updated);
        return updated.toBuilder().build();
    }

    @Override
    public boolean delete(Long id) {
        return queue.removeIf(task -> task.getId().equals(id));
    }

    @Override
    public List<Task> getAll() {
        Collections.sort(queue);
        return queue.stream().map(task -> task.toBuilder().build()).collect(Collectors.toList());
    }

    @Override
    public Task changePriority(Long id, Priority priority) {
        if (priority == null) {
            throw new NullPointerException("com.tdd.Priority cannot be null");
        }

        return update(id, null, null, null, priority);
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
