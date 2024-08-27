//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mordonia.mordoniasupport.util;

import java.util.UUID;

public class TicketData {
    private String issue;
    private String status;
    private Integer id;
    private UUID player;
    private UUID staff;
    private String name;

    public TicketData(Integer id, UUID player, String issue, String status, UUID staff, String name) {
        this.id = id;
        this.player = player;
        this.issue = issue;
        this.status = status;
        this.staff = staff;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssue() {
        return this.issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public UUID getStaff() {
        return this.staff;
    }

    public void setStaff(UUID staff) {
        this.staff = staff;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
