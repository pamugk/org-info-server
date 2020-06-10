package ru.psu.org_info_server.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TreeNode<T> {
    private T value;
    private boolean hasChildren;
}
