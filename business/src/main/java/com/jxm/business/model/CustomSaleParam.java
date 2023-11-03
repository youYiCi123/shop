package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CustomSaleParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long salesPersonId;
    private String salesPersonName;
    private String salesPersonEmail;
    private String salesPersonPhone;
}
