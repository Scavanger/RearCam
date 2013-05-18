package com.scavanger.gpio;

import java.util.EventListener;

public interface GPIOListener extends EventListener {
	void onGPIOChanged (GPIOEvent e);
}