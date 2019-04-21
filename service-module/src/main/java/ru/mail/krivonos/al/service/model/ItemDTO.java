package ru.mail.krivonos.al.service.model;

import ru.mail.krivonos.al.repository.model.ItemStatusEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ItemDTO {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String name;

    private ItemStatusEnum status;

    public ItemDTO() {
    }

    public ItemDTO(@NotNull @Size(max = 40) String name, ItemStatusEnum status) {
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ItemStatusEnum status) {
        this.status = status;
    }
}
