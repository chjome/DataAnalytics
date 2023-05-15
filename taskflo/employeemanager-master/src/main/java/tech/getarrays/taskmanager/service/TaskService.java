package tech.getarrays.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.getarrays.taskmanager.exception.UserNotFoundException;
import tech.getarrays.taskmanager.model.Task;
import tech.getarrays.taskmanager.repo.TaskRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaskService {
    // Below line injects TaskRepo interface
    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }
    // The above line is for constructing

    public Task addTask(Task task) {
        // setTaskCode is from the Task class getters/setters
        task.setTaskCode(UUID.randomUUID().toString());
        // save comes from the JpaRepository that's part of the TaskRepo interface
        return taskRepo.save(task);
    }

    public List<Task> findAllTasks() {
        return taskRepo.findAll();
    }

    public Task updateTask(Task task) {
        return taskRepo.save(task);
    }

    public Task findTaskById(Long id) {
        return taskRepo.findTaskById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
        // The above lines look for an task id, but if not found throws an exception
        // The UserNotFoundException had to be created in the "exception" folder
    }

    public void deleteTask(Long id){
        taskRepo.deleteTaskById(id);
    }
    // The above works because spring understands the language due to it being in what's called "query method"
    // so spring automatically knows what it's trying to do and does it
}
