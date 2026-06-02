package com.pitstop.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DigitalPassport {
    private int id;
    private User owner;
    private LocalDate issueDate;
    private boolean isPhysicalEligible;
    private boolean isPhysicalClaimed;
    private List<Stamp> stamps;

    public DigitalPassport(int id, User owner) {
        this.id = id;
        this.owner = owner;
        this.issueDate = LocalDate.now();
        this.isPhysicalEligible = false;
        this.isPhysicalClaimed = false;
        this.stamps = new ArrayList<>();
    }

    public void addStamp(Stamp stamp) {
        stamps.add(stamp);
        owner.setTotalStamps(owner.getTotalStamps() + 1);
        if (owner.getTotalStamps() >= 100) {
            isPhysicalEligible = true;
        }
    }

    public int countTotalStamps() {
        return stamps.size();
    }

    public boolean checkEligibility() {
        return owner.getTotalStamps() >= 100;
    }

    public boolean requestPhysicalPassport() {
        if (isPhysicalEligible && !isPhysicalClaimed) {
            isPhysicalClaimed = true;
            return true;
        }
        return false;
    }

    public int getId() { return id; }
    public User getOwner() { return owner; }
    public boolean isPhysicalEligible() { return isPhysicalEligible; }
    public boolean isPhysicalClaimed() { return isPhysicalClaimed; }
    public List<Stamp> getStamps() { return stamps; }
    public LocalDate getIssueDate() { return issueDate; }
}
