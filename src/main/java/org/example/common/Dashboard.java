package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Dashboard {
    private Long tech_id;
    private String name;
    private String category;
    private String section;
    private String ring;
    private Map<String,Integer> votes;
}
