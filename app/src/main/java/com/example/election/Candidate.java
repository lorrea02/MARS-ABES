package com.example.election;

public class Candidate {
    private int candidateID;
    private String name;
    private String position;
    private int currentVotes;

    public Candidate(int candidateID, String name, String position, int currentVotes) {
        this.candidateID = candidateID;
        this.name = name;
        this.position = position;
        this.currentVotes = currentVotes;
    }

    public int getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(int candidateID) {
        this.candidateID = candidateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getCurrentVotes() {
        return currentVotes;
    }

    public void setCurrentVotes(int currentVotes) {
        this.currentVotes = currentVotes;
    }
}
