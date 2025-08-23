package net.redstone233.test.core.api;

public interface PlayerDataProvider {
    int getLevel();
    int getExperience();
    boolean isVip();
    boolean isSVip();
    int getExperienceForNextLevel();

    default float getExperienceProgress() {
        int current = getExperience();
        int nextLevelExp = getExperienceForNextLevel();
        return nextLevelExp > 0 ? (float) current / nextLevelExp : 0;
    }
}