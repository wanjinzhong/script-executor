package com.synnex.shellexecutor.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_history")
@Getter
@Setter
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_id")
    private Integer taskId;
    @Column
    private String user;
    @Column
    private String ip;
    @Column
    private LocalDateTime datetime;
    @OneToMany(targetEntity = HistoryParam.class, mappedBy = "history", cascade = CascadeType.ALL)
    private List<HistoryParam> params = new ArrayList<>();
}