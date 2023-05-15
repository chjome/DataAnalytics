package tech.getarrays.taskmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.getarrays.taskmanager.model.Task;

import java.util.Optional;

// JpaRepository is a class that handles saving/loading stuff
public interface TaskRepo extends JpaRepository<Task, Long> {
    void deleteTaskById(Long id);

    // Optional makes it so it doesn't break if not passed an id, ie when no task found
    Optional<Task> findTaskById(Long id);
}
