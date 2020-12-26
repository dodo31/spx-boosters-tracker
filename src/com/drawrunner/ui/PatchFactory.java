package com.drawrunner.ui;
import java.awt.Color;

import com.drawrunner.ui.status.Mission;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PatchFactory {
	public static PGraphics createHalphStripedPatch(PApplet p, float stripsAngle, float stripsHeight, float stripsRatio, PVector brightnessRange) {
		PGraphics partialFailureTexture = createStripedTexture(p, stripsAngle, stripsHeight, stripsRatio, brightnessRange, new PVector(Mission.DEFAULT_SIZE, Mission.DEFAULT_SIZE * 2));
		
		float cornersRadius = Mission.DEFAULT_SIZE / 5f;
		PGraphics patchHalphMask = createRoundedCorner(p, new PVector(Mission.DEFAULT_SIZE / 2f, Mission.DEFAULT_SIZE), 0, cornersRadius, cornersRadius, 0);
		
		return combineTextureAndMask(p, partialFailureTexture, patchHalphMask);
	}
	
	public static PGraphics createFullStripedPatch(PApplet p, float stripsAngle, float stripsHeight, float stripsRatio, PVector brightnessRange) {
		PGraphics partialFailureTexture = createStripedTexture(p, stripsAngle, stripsHeight, stripsRatio, brightnessRange, new PVector(Mission.DEFAULT_SIZE * 2, Mission.DEFAULT_SIZE * 2));
		PGraphics patchFullMask = createRoundedCorner(p, new PVector(Mission.DEFAULT_SIZE, Mission.DEFAULT_SIZE), Mission.DEFAULT_SIZE / 5f);
		return combineTextureAndMask(p, partialFailureTexture, patchFullMask);
	}
	
	private static PGraphics createStripedTexture(PApplet p, float stripsAngle, float stripsHeight, float stripsRatio, PVector brightnessRange, PVector size) {
		PGraphics stripedTexture = p.createGraphics((int) size.x, (int) size.y, PApplet.P2D);
		stripedTexture.beginDraw();
		stripedTexture.pushMatrix();
		stripedTexture.translate(size.x / 2f, size.y / 2f);
		stripedTexture.rotate(PApplet.radians(stripsAngle));
		stripedTexture.translate(-size.x / 2f, -size.y / 2f);

		stripedTexture.noStroke();
		
		float sectionHeight = stripsHeight * (1 / (stripsRatio * 1f));
		for(float y = -size.y; y < size.y * 2; y += sectionHeight) {
			stripedTexture.fill(brightnessRange.x);
			stripedTexture.rect(-size.x, y + stripsHeight, size.x * 2, sectionHeight - stripsHeight);
			
			stripedTexture.fill(brightnessRange.y);
			stripedTexture.rect(-size.x, y, size.x * 2, stripsHeight);
		}
		
		stripedTexture.popMatrix();
		stripedTexture.endDraw();
		
		return stripedTexture;
	}
	
	private static PGraphics createRoundedCorner(PApplet p, PVector textureSize, float cornersRadius) {
		return createRoundedCorner(p, textureSize, cornersRadius, cornersRadius, cornersRadius, cornersRadius);
	}
	
	private static PGraphics createRoundedCorner(PApplet p, PVector textureSize, float topLeftCorner, float topRightCorner, float botRightCorner, float botLeftCorner) {
		PGraphics patchMask = p.createGraphics((int) textureSize.x, (int) textureSize.y, PApplet.P2D);
		
		patchMask.beginDraw();
		patchMask.noStroke();
		patchMask.background(0);
		patchMask.fill(255);
		patchMask.rect(0, 0, patchMask.width, patchMask.height, topLeftCorner, topRightCorner, botRightCorner, botLeftCorner);
		patchMask.endDraw();
		
		return patchMask;
	}
	
	private static PGraphics combineTextureAndMask(PApplet p, PImage texture, PImage mask) {
		return combineTextureAndMask(p, texture, mask, Color.WHITE);
	}
	
	private static PGraphics combineTextureAndMask(PApplet p, PImage texture, PImage mask, Color tint) {
		PGraphics texturedShape = p.createGraphics(mask.width, mask.height, PApplet.P2D);
		
		texturedShape.beginDraw();
		texturedShape.loadPixels();
		texture.loadPixels();
		mask.loadPixels();
		
		for (int i = 0; i < mask.pixels.length; i++) {
			int patchPosX = i % mask.width;
			int patchPosY = (int) ((i - patchPosX) / (mask.width * 1f));
			
			int textureIndex = patchPosY * texture.width + patchPosX;
			
			Color texturePixelColor = new Color(texture.pixels[textureIndex]);
			int texturePixelBrightness = (int) texturedShape.brightness(mask.pixels[i]);
			
			int redValue = (int) (tint.getRed() * (texturePixelColor.getRed() / 255f));
			int greenValue = (int) (tint.getGreen() * (texturePixelColor.getGreen() / 255f));
			int blueValue = (int) (tint.getBlue() * (texturePixelColor.getBlue() / 255f));
			
			texturedShape.pixels[i] = new Color(redValue, greenValue, blueValue, texturePixelBrightness).getRGB();
		}
		
		texturedShape.updatePixels();
		texturedShape.endDraw();
		
		return texturedShape;
	}
}
