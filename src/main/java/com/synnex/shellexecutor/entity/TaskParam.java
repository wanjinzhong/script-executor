package com.synnex.shellexecutor.entity;

import com.synnex.shellexecutor.enums.ParamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "task_param")
@Getter
@Setter
public class TaskParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private ParamType type;
    @Column(name = "available_value")
    private String availableValue;
    @Column(name = "default_value")
    private String defaultValue;
    @Column
    private String seq;

    public List<String> getAvailableValue() {
        return availableValue == null || availableValue.trim().length() == 0 ? new ArrayList<>() :
                Arrays.stream(availableValue.split(",")).map(String::trim).distinct().collect(Collectors.toList());
    }
}
