package com.drawrunner.ui;
import java.awt.Color;

public interface ColorTemplate {
	public static final Color BACKGROUND = new Color(24, 28, 31);
	public static final Color SEPARATOR = new Color(166, 166, 166);
	
	public static final Color LIFTOFF_SUCCESS = new Color(75, 191, 255);
	public static final Color LIFTOFF_PARTIAL_FAILURE = new Color(246, 201, 74);
	public static final Color LIFTOFF_TOTAL_FAILURE = new Color(247, 93, 93);
	
	public static final Color LANDING_SUCCESS = new Color(144, 242, 78);
	public static final Color LANDING_OCEAN_LANDING = new Color(246, 201, 74);
	public static final Color LANDING_TOTAL_FAILURE = new Color(247, 93, 93);
	
	public static final Color FUTURE_MISSION_BACKGROUND = new Color(70, 70, 70, 127);
	public static final Color FUTURE_MISSION_STROKE = new Color(84, 84, 84);
	
	public static final Color PATCH_SYMBOL_TINT = new Color(0, 0, 0, 0.56f);
	public static final Color PATCH_SYMBOL_BRIGHT_TINT = new Color(1, 1, 1, 0.28f);

	public static final Color LIFTOFF_INFO = new Color(144, 242, 78);
	public static final Color LANDING_INFO = new Color(75, 191, 255);
	public static final Color PAD_INFO = new Color(246, 198, 64);
	public static final Color PAYLOAD_INFO = new Color(237, 93, 93);
	
	public static final Color ODD_BOOSTER_SECTION_BACKGROUND = new Color(30, 34, 36);
	public static final Color EVEN_BOOSTER_SECTION_BACKGROUND = BACKGROUND;
}
