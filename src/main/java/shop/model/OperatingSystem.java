/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author HoangSang
 */
public class OperatingSystem {
     private int osId;
    private Integer gameDetailsId;
    private String osName;

    public OperatingSystem() {
    }

    public OperatingSystem(int osId, String osName) {
        this.osId = osId;
        this.osName = osName;
    }

    public OperatingSystem(int osId, Integer gameDetailsId, String osName) {
        this.osId = osId;
        this.gameDetailsId = gameDetailsId;
        this.osName = osName;
    }

    public int getOsId() {
        return osId;
    }

    public void setOsId(int osId) {
        this.osId = osId;
    }

    public Integer getGameDetailsId() {
        return gameDetailsId;
    }

    public void setGameDetailsId(Integer gameDetailsId) {
        this.gameDetailsId = gameDetailsId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
