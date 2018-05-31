package com.deepika.keralaelectionlive;

public class CandidateVotes {
    int vote_candidate_id;
    int vote_candidate_vote;

    public CandidateVotes(int vote_candidate_id, int vote_candidate_vote) {
        this.vote_candidate_id = vote_candidate_id;
        this.vote_candidate_vote = vote_candidate_vote;
    }

    public CandidateVotes() {
    }

    public int getVote_candidate_id() {
        return vote_candidate_id;
    }

    public void setVote_candidate_id(int vote_candidate_id) {
        this.vote_candidate_id = vote_candidate_id;
    }

    public int getVote_candidate_vote() {
        return vote_candidate_vote;
    }

    public void setVote_candidate_vote(int vote_candidate_vote) {
        this.vote_candidate_vote = vote_candidate_vote;
    }
}
