package com.me.geonauts.model;

import com.me.geonauts.model.enums.Achievement;

public interface ActionResolver {
	public void Login();
	public void LogOut();
	
	//get if client is signed in to Google+
	public boolean isSignedIn();
	
	//submit a score to a leaderboard
	public void submitScore(int score);
	
	//gets the scores and displays them through googles default widget
	public void showLeaderboard();
	
	//gets the score and gives access to the raw score data
	public void getScoresData();
	
	/** Unlocks given achievement */
	public void unlockAchievement(Achievement a);
	
	/** Increments given achievement */
	public void incrementAchievement(Achievement a, int value);
	
	/** Displays achievements */
	public void showAchievements();
}