package com.synnex.shellexecutor.entity;

import com.synnex.shellexecutor.enums.YorN;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_group")
@Getter
@Setter
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private YorN expand;
    @OneToMany(targetEntity = Task.class, mappedBy = "group")
    private List<Task> tasks = new ArrayList<>();
}
