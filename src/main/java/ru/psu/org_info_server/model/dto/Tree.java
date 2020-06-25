package ru.psu.org_info_server.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Tree<T> {
    private List<TreeNode<T>> nodes;
    private int totalCount;
}
