package com.me.geonauts;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.me.geonauts.Geonauts;

public class GeonautsDesktop {

	public static void main(String[] args) {
		new LwjglApplication(new Geonauts(), "Geonauts", 1280, 720, true); //800,480, true);
	}

}
