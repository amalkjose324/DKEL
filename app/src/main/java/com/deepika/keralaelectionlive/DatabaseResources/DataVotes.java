package com.deepika.keralaelectionlive.DatabaseResources;

public class DataVotes {
    int vote_candidate_id;
    int vote_candidate_vote;

    public DataVotes(int vote_candidate_id, int vote_candidate_vote) {
        this.vote_candidate_id = vote_candidate_id;
        this.vote_candidate_vote = vote_candidate_vote;
    }

    public DataVotes() {
    }

    public int getVote_candidate_id() {
        return vote_candidate_id;
    }

    public void setVote_candidate_id(int vote_candidate_id) {
        this.vote_candidate_id = vote_candidate_id;
    }

    public int getvote_candidate_vote() {
        return vote_candidate_vote;
    }

    public void setvote_candidate_vote(int vote_candidate_vote) {
        this.vote_candidate_vote = vote_candidate_vote;
    }
}
