package ru.mail.krivonos.al.repository.model;

import java.util.Date;

public class AuditItem {

    private Long id;

    private Long itemID;

    private AuditItemActionEnum action;

    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public AuditItemActionEnum getAction() {
        return action;
    }

    public void setAction(AuditItemActionEnum action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AuditItem{" +
                "id=" + id +
                ", itemID=" + itemID +
                ", action=" + action +
                ", date=" + date +
                '}';
    }
}
