package com.me.geonauts.model;

public class DesktopInterface implements ActionResolver {

	@Override
	public void Login() {
		System.out.println("Desktop: would of logged in here");
	}

	@Override
	public void LogOut() {
		System.out.println("Desktop: would of logged out here");
	}

	@Override
	public boolean isSignedIn() {
		System.out.println("Desktop: getSignIn()");
		return false;
	}

	public void submitScore(int score) {
		System.out.println("Desktop: submitScore: " + score);
	}

	@Override
	public void showLeaderboard() {
		System.out.println("Desktop: getScores()");
	}

	@Override
	public void getScoresData() {
		System.out.println("Desktop: getScoresData()");
	}

	@Override
	public void unlockAchievement(Achievement a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAchievements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementAchievement(Achievement a, int value) {
		// TODO Auto-generated method stub
		
	}

}
