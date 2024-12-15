package controller;

public interface Subject {
    void addObs(Observer observer);

    void removeObs(Observer observer);

    void notifyObs();
}
