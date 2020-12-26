package com.drawrunner.data.entities;

public abstract class SpaceEntity {
	protected String id;

	public SpaceEntity(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
}
