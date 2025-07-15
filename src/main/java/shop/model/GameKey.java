/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author HoangSang
 */
public class GameKey {
    private int gameKeyId;
    private int gameDetailsId;
    private String keyCode;

    public GameKey() {
    }

    public GameKey(int gameKeyId, int gameDetailsId, String keyCode) {
        this.gameKeyId = gameKeyId;
        this.gameDetailsId = gameDetailsId;
        this.keyCode = keyCode;
    }
    

    public GameKey(int gameKeyId, String keyCode) {
        this.gameKeyId = gameKeyId;
        this.keyCode = keyCode;
    }

    public int getGameKeyId() {
        return gameKeyId;
    }

    public void setGameKeyId(int gameKeyId) {
        this.gameKeyId = gameKeyId;
    }

    public int getGameDetailsId() {
        return gameDetailsId;
    }

    public void setGameDetailsId(int gameDetailsId) {
        this.gameDetailsId = gameDetailsId;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
    
}
