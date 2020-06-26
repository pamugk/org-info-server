package ru.psu.org_info_server.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tree<T> {
    private List<TreeNode<T>> nodes;
    private int totalCount;
}
