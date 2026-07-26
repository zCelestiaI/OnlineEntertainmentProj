package com.example.java4main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteReportDTO {
    private String videoTitle;
    private Long favoriteCount;
    private Date newestDate;
    private Date oldestDate;
}
