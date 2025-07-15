/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author PC
 */
public class Voucher {

    private int voucherId;
    private String code;
    private BigDecimal value;
    private int usageLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private int active;
    private String description;
    private BigDecimal minOrderValue;

    public Voucher(String code, BigDecimal value, int usageLimit, LocalDate startDate, LocalDate endDate, int active, String description, BigDecimal minOrderValue) {
        this.code = code;
        this.value = value;
        this.usageLimit = usageLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.description = description;
        this.minOrderValue = minOrderValue;
    }

    public Voucher(int voucherId) {
        this.voucherId = voucherId;
    }

    // Constructors
    public Voucher() {
    }

    public Voucher(int voucherId, String code, BigDecimal value, int usageLimit, LocalDate startDate,
            LocalDate endDate, int active, String description, BigDecimal minOrderValue) {
        this.voucherId = voucherId;
        this.code = code;
        this.value = value;
        this.usageLimit = usageLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.description = description;
        this.minOrderValue = minOrderValue;
    }

    // Getters and Setters
    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

  

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }
}
