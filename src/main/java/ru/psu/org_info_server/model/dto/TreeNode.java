package ru.psu.org_info_server.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<T> {
    private T value;
    private boolean hasChildren;
}
