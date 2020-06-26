package ru.psu.org_info_server.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListChunk<T> {
    private int totalCount;
    private List<T> dataChunk;
}
