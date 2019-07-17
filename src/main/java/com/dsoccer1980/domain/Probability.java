package com.dsoccer1980.domain;

public enum Probability {

    PIECE_OF_CAKE("Piece of cake"),
    WALK_IN_THE_PARK("Walk in the park"),
    SURE_THING("Sure thing"),
    QUITE_LIKELY("Quite likely"),
    HMMM("Hmmm...."),
    GAMBLE("Gamble"),
    RISKY("Risky"),
    PLAYING_WITH_FIRE("Playing with fire"),
    RATHER_DETRIMENTAL("Rather detrimental"),
    IMPOSSIBLE("Impossible"),
    SUICIDE_MISSION("Suicide mission");

    private String probability;

    Probability(String probability) {

        this.probability = probability;
    }

    public String getProbability() {
        return probability;
    }
}
