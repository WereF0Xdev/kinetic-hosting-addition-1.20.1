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

	@Override
	public void onInitializeClient() {
		JsonObject config = loadConfig();
		affiliateLink = config.get("affiliate_link").getAsString();

		System.out.println("Loaded Affiliate Link: " + affiliateLink);

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

		try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
			GSON.toJson(defaultConfig, writer);
		}
	}

	public static String getAffiliateLink() {
		return affiliateLink;
	}
}