package com.jxm.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SupplierBriefDto {
   private String  supplierName; //供应商名称
   private Date businessAuthTime; //经营授权有效期至
}
