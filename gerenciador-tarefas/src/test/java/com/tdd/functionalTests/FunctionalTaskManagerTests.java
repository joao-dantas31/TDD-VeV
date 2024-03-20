package com.tdd.functionalTests;

import com.tdd.Priority;
import com.tdd.Task;
import com.tdd.TaskManager;
import com.tdd.TaskManagerImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionalTaskManagerTests {

    private TaskManager manager;

    @BeforeEach
    void setup() {
        manager = new TaskManagerImp();
    }

    @Test
    @DisplayName("Testa se o gerenciador consegue criar uma tarefa. T1")
    void testCreate() {
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(), Priority.LOW);

        assertFalse(manager.isEmpty());
    }

    @Test
    @DisplayName("Testa com valores nulos e vazios durante a criação. T2-6, T10")
    void testNullAndEmptyCreate() {
        assertThrows(NullPointerException.class , () -> manager.create(null, "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW));
        assertThrows(NullPointerException.class , () -> manager.create("Tarefa 1", null, new Date(999216000000L), Priority.LOW));
        assertThrows(NullPointerException.class , () -> manager.create("Tarefa 1", "Descrição da tarefa 1", null, Priority.LOW));
        assertThrows(NullPointerException.class , () -> manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), null));

        manager.create("", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);
        manager.create("Tarefa 1", "", new Date(999216000000L), Priority.LOW);
    }

    @Test
    @DisplayName("Testa criação com diferentes datas. T7-9")
    void testDatesOnCreates() throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        manager.create("Tarefa 1", "Descrição da tarefa 1", format.parse("31/12/2020"), Priority.LOW);
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(), Priority.LOW);
        manager.create("Tarefa 1", "Descrição da tarefa 1", format.parse("31/12/2027"), Priority.LOW);
    }

    @Test
    @DisplayName("Testa se o gerenciador consegue atualizar os dados. T11")
    void testUpdate() {
        Task task = manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);

        Task updatedTask = manager.update(task.getId(), "Tarefa 3", "Descrição da tarefa 3", new Date(196327953000L), Priority.HIGH);

        assertEquals("Tarefa 3", updatedTask.getTitle());
        assertEquals("Descrição da tarefa 3", updatedTask.getDescription());
        assertEquals(new Date(196327953000L), updatedTask.getDueDate());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
        assertEquals(1, manager.size());
    }

    @Test
    @DisplayName("Testa se o gerenciador falha ao não atualizar nenhum dado. T12")
    void testNullUpdate() {
        Task task = manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);

        Task updatedTask = manager.update(task.getId(), null, null, null, null);

        assertEquals(task, updatedTask);
    }

    @Test
    @DisplayName("Testa se o gerenciador lança exceção ao tentar atualizar uma tarefa inválida. T13")
    void testNotFoundUpdate() {
        assertThrows(NoSuchElementException.class, () -> manager.update(1111L, null, null, null, null));
    }

    @Test
    @DisplayName("Testa se o gerenciador apaga tarefas corretamente. 14")
    void testDelete() {
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);
        Task toDelete = manager.create("Tarefa 2", "Descrição da tarefa 2", new Date(1356609600000L), Priority.MEDIUM);
        manager.create("Tarefa 3", "Descrição da tarefa 3", new Date(196327953000L), Priority.HIGH);

        assertEquals(3, manager.size());

        assertTrue(manager.delete(toDelete.getId()));

        assertEquals(2, manager.size());

        assertFalse(manager.getAll().stream().anyMatch(task -> task.getTitle().equals("Tarefa 2") && task.getDescription().equals("Descrição da tarefa 2") && task.getDueDate().equals(new Date(1356609600000L)) && task.getPriority().equals(Priority.MEDIUM)));
    }

    @Test
    @DisplayName("Testa se o gerenciador retorna falso ao apagar uma tarefa inexistente. 15")
    void testNotFoundDelete() {
        assertFalse(manager.delete(1111L));
    }

    @Test
    @DisplayName("Testa se o gerenciador ordena os dados por data. T16")
    void testDateGet() {
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);
        manager.create("Tarefa 2", "Descrição da tarefa 2", new Date(1356609600000L), Priority.LOW);

        assertEquals(2, manager.size());

        List<Task> tasks = manager.getAll();

        assertEquals(manager.size(), tasks.size());

        assertEquals("Tarefa 1", tasks.get(0).getTitle());
        assertEquals("Tarefa 2", tasks.get(1).getTitle());
    }

    @Test
    @DisplayName("Testa se o gerenciador ordena os dados por prioridade e data. T17")
    void testDateAndPriorityGet() {
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);
        manager.create("Tarefa 2", "Descrição da tarefa 2", new Date(1356609600000L), Priority.MEDIUM);
        manager.create("Tarefa 3", "Descrição da tarefa 3", new Date(196327953000L), Priority.HIGH);
        manager.create("Tarefa 4", "Descrição da tarefa 4", new Date(999206000000L), Priority.LOW);
        manager.create("Tarefa 5", "Descrição da tarefa 5", new Date(1356709600000L), Priority.MEDIUM);
        manager.create("Tarefa 6", "Descrição da tarefa 6", new Date(186327953000L), Priority.HIGH);

        assertEquals(6, manager.size());

        List<Task> tasks = manager.getAll();

        assertEquals(manager.size(), tasks.size());

        assertEquals("Tarefa 6", tasks.get(0).getTitle());
        assertEquals("Tarefa 3", tasks.get(1).getTitle());
        assertEquals("Tarefa 2", tasks.get(2).getTitle());
        assertEquals("Tarefa 5", tasks.get(3).getTitle());
        assertEquals("Tarefa 4", tasks.get(4).getTitle());
        assertEquals("Tarefa 1", tasks.get(5).getTitle());
    }

    @Test
    @DisplayName("Testa se o gerenciador ordena os dados por prioridade. T18")
    void testPriorityGet() {
        manager.create("Tarefa 1", "Descrição da tarefa 1", new Date(999216000000L), Priority.LOW);
        manager.create("Tarefa 2", "Descrição da tarefa 2", new Date(999216000000L), Priority.HIGH);

        assertEquals(2, manager.size());

        List<Task> tasks = manager.getAll();

        assertEquals(manager.size(), tasks.size());

        assertEquals("Tarefa 2", tasks.get(0).getTitle());
        assertEquals("Tarefa 1", tasks.get(1).getTitle());
    }

}

