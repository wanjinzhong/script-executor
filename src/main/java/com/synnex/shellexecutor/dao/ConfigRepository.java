package com.synnex.shellexecutor.dao;

import com.synnex.shellexecutor.entity.Config;
import com.synnex.shellexecutor.enums.ConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {
    List<Config> findByKey(ConfigKey key);
}
