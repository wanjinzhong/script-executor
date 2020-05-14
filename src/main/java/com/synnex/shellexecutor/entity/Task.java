package com.synnex.shellexecutor.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    private String seq;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String script;
    @Column(name = "success_msg")
    private String successMsg;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private TaskGroup group;
    @Column(name = "last_run_time")
    private LocalDateTime lastRunTime;
}
