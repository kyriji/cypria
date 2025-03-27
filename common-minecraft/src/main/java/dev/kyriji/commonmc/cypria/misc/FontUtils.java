package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FontUtils {
	private static Map<Character, Integer> characterWidths;

	private static final Logger logger = Logger.getLogger("FontUtils");
	private static boolean initialized = false;

	public static void initFont() {
		try {
			InputStream stream = FontUtils.class.getClassLoader().getResourceAsStream("mojangles_char_width.txt");

			if (stream == null) {
				logger.warning("mojangles_char_width.txt not found in resources");
				return;
			}

			characterWidths = new HashMap<>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(",", 2);
				if (parts.length == 2) {
					char character = line.charAt(0);
					int width = Integer.parseInt(line.substring(2));
					characterWidths.put(character, width);
				}
			}

			reader.close();
			initialized = true;
			logger.info("minecraft font data loaded successfully with " + characterWidths.size() + " characters");
		} catch (Exception e) {
			logger.severe("error loading font data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static int getStringWidth(String rawText, boolean bold) {
		if (!initialized) {
			logger.warning("font not initialized, call initFont() first");
			return -1;
		}

		int width = 0;
		for (int i = 0; i < rawText.length(); i++) {
			char character = rawText.charAt(i);
			width += getCharWidth(character);
		}

		width += rawText.length() - 1;
		if (bold) width += rawText.length();
		return width;
	}

	private static int getCharWidth(char character) {
		if (characterWidths.containsKey(character)) return characterWidths.get(character);
		return 5;
	}

	public static String createHeader(ChatColor primary, ChatColor secondary, int spaces, String centerText) {
		String primaryString = "&" + primary.getChar();
		String secondaryString = "&" + secondary.getChar();

		String header = secondaryString +
				"&l" +
				"&m ".repeat(Math.max(0, spaces)) +
				secondaryString +
				"&l<" +
				primaryString +
				"&l " +
				centerText.toUpperCase() +
				secondaryString +
				"&l >" +
				"&m ".repeat(Math.max(0, spaces));

		return AUtil.colorize(header);
	}

	public static String createPlainFooter(ChatColor secondary, String titleHeader) {
		int width = getStringWidth(ChatColor.stripColor(titleHeader), true);
		int bolded = (width - 4) % 4 + 1;
		int normal = (width / 4) - bolded;

		return secondary +
				"&m ".repeat(normal) +
				"&l" +
				"&m ".repeat(bolded);
	}
}