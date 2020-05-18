package com.synnex.shellexecutor.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.tags.Param;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "history_param")
@Getter
@Setter
public class HistoryParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "history_id")
    private TaskHistory history;
    @ManyToOne
    @JoinColumn(name = "param_id")
    private TaskParam param;
    @Column
    private String value;
}
