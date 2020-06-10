package ru.psu.org_info_server.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListChunk<T> {
    private int totalCount;
    private List<T> dataChunk;
}
