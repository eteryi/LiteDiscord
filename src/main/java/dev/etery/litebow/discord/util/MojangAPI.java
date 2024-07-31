package dev.etery.litebow.discord.util;

import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class MojangAPI {
    private static final HashMap<String, UUID> username2uuidCache = new HashMap<>();

    private static class MojangUserResponse {
        private final String id;
        private final String name;

        private MojangUserResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static @Nullable UUID getUUIDFromUsername(String username) {
        if (username2uuidCache.get(username) != null) {
            return username2uuidCache.get(username);
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                // TODO replace Scanner with BufferedReader
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNext()) {
                    builder.append(scanner.nextLine());
                }
                String rawJson = builder.toString();
                MojangUserResponse response = new GsonBuilder().create().fromJson(rawJson, MojangUserResponse.class);
                UUID uuid = UUID.fromString(response.id.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                ));
                username2uuidCache.put(username, uuid);
                return uuid;
            }
            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
