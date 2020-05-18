package com.synnex.shellexecutor.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pageable<T> {
    private long totalSize;
    private int totalPage;
    private int page;
    private int size;
    private List<T> data;
}
