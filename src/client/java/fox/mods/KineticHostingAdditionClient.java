package fox.mods;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class KineticHostingAdditionClient implements ClientModInitializer {
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("kinetic_hosting_affiliate_config.json");
	private static final Gson GSON = new Gson();
	private static final String DEFAULT_AFFILIATE_LINK = "https://default.affiliatelink.com";

	private static String affiliateLink;
	private static boolean displayLogo;
	private static int logoX, logoY;
	private static double logoScale;

	@Override
	public void onInitializeClient() {
		JsonObject config = loadConfig();

		affiliateLink = config.get("affiliate_link").getAsString();
		displayLogo = config.has("display_logo") && config.get("display_logo").getAsBoolean();
		logoX = config.has("logo_x") ? config.get("logo_x").getAsInt() : 0;
		logoY = config.has("logo_y") ? config.get("logo_y").getAsInt() : 0;
		logoScale = config.has("logo_scale") ? config.get("logo_scale").getAsDouble() : 1.0;

		System.out.println("Loaded Affiliate Link: " + affiliateLink);
		System.out.println("Display Logo: " + displayLogo);
		System.out.println("Logo Position: (" + logoX + ", " + logoY + ")");
		System.out.println("Logo Scale: " + logoScale);
	}

	private static JsonObject loadConfig() {
		try {
			if (!Files.exists(CONFIG_PATH)) {
				createDefaultConfig();
			}
			String content = Files.readString(CONFIG_PATH);
			return JsonParser.parseString(content).getAsJsonObject();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load affiliate config!", e);
		}
	}

	private static void createDefaultConfig() throws IOException {
		JsonObject defaultConfig = new JsonObject();
		defaultConfig.addProperty("affiliate_link", DEFAULT_AFFILIATE_LINK);
		defaultConfig.addProperty("display_logo", true);
		defaultConfig.addProperty("logo_x", 0);
		defaultConfig.addProperty("logo_y", 0);
		defaultConfig.addProperty("logo_scale", 4.13);

		try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
			GSON.toJson(defaultConfig, writer);
		}
	}

	public static String getAffiliateLink() {
		return affiliateLink;
	}

	public static boolean shouldDisplayLogo() {
		return displayLogo;
	}

	public static int getLogoX() {
		return logoX;
	}

	public static int getLogoY() {
		return logoY;
	}

	public static double getLogoScale() {
		return logoScale;
	}
}
