package com.synnex.shellexecutor.dao;

import com.synnex.shellexecutor.entity.IpTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpTableRepository extends JpaRepository<IpTable, Integer> {
    List<IpTable> findByIp(String ip);
}
