package com.synnex.shellexecutor.entity;

import com.synnex.shellexecutor.enums.ConfigKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    @Enumerated(EnumType.STRING)
    private ConfigKey key;
    @Column
    private String value;
    @Column
    private Integer seq;
}
