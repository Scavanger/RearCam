package com.scavanger.gpio;

import java.util.EventObject;

public class GPIOEvent extends EventObject{

	private static final long serialVersionUID = 7648574259808415623L;
	private boolean on;
	
	public GPIOEvent(Object source, boolean on) {
		super(source);
		this.on = on;
	}
	
	public boolean isOn() { return on; }

}

