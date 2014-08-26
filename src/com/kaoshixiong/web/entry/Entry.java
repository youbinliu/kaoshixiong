package com.kaoshixiong.web.entry;

import com.jfinal.core.JFinal;

public class Entry {

	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}

}
