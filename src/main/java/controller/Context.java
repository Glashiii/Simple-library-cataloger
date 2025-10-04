package controller;

import lombok.Getter;

@Getter
public class Context {
    private Integer currentCabinetId = null;
    private Integer currentShelfId = null;

    public boolean isInRoom() {
        return currentCabinetId == null && currentShelfId == null;
    }

    public boolean isInCabinet() {
        return currentCabinetId != null && currentShelfId == null;
    }

    public boolean isInShelf() {
        return currentShelfId != null;
    }

    public boolean isAtRoot() {
        return !isInCabinet() && !isInShelf();
    }

    public void setCabinet(int id) {
        this.currentCabinetId = id;
        this.currentShelfId = null;
    }

    public void setShelf(int id) {
        this.currentShelfId = id;
    }

    public void back() {
        if (isInShelf()) currentShelfId = null;
        else if (isInCabinet()) currentCabinetId = null;
    }

}
