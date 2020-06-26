package ru.psu.org_info_server.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private T data;
}
