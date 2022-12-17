package com.example.schoolproject.Modal;

public class GoalModal {
    private String gaol;
    private boolean doneAch;

    public GoalModal(String gaol, boolean doneAch) {
        this.gaol = gaol;
        this.doneAch = doneAch;
    }

    public String getGaol() {
        return gaol;
    }

    public void setGaol(String gaol) {
        this.gaol = gaol;
    }

    public boolean isDoneAch() {
        return doneAch;
    }

    public void setDoneAch(boolean doneAch) {
        this.doneAch = doneAch;
    }
}
