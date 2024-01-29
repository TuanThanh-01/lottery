package com.qldt.lottery.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultRequest {
    private String prize;
    private String employeeID;
    private String email;
    private String name;

}
