package dev.kyriji.objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PitConfig {
	private static final Gson GSON = new Gson();

	public static final BuilderCodec<PitConfig> CODEC = BuilderCodec.builder(PitConfig.class, PitConfig::new)
			.append(
					new KeyedCodec<>("ConfigMongoURI", Codec.STRING),
					(config, value, extraInfo) -> config.mongoConfigURI = value,
					(config, extraInfo) -> config.mongoConfigURI
			)
			.add()
			.append(
					new KeyedCodec<>("ConfigMongoDatabase", Codec.STRING),
					(config, value, extraInfo) -> config.mongoConfigDatabase = value,
					(config, extraInfo) -> config.mongoConfigDatabase
			)
			.add()
			.build();

	@SerializedName("mongoConfigURI")
	public String mongoConfigURI;

	@SerializedName("mongoConfigDatabase")
	public String mongoConfigDatabase;

	public JsonObject toJsonObject() {
		return GSON.toJsonTree(this).getAsJsonObject();
	}
}
