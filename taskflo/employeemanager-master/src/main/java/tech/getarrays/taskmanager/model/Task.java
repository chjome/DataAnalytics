package tech.getarrays.taskmanager.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false) //This makes it so that "id" can't be changed once instantiated
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String color;
    @Column(nullable = false, updatable = false) //This makes it so that "taskCode" can't be changed once instantiated
    private String taskCode;

    public Task() {}

    // Lotta the stuff below this point are getters and setters

    public Task(String name, String startDate, String endDate, String priority, String color, String taskCode) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
        this.taskCode = taskCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start date='" + startDate + '\'' +
                ", end date='" + endDate + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
