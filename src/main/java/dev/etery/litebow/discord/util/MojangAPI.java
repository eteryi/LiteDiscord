package dev.etery.litebow.discord.util;

import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class MojangAPI {
    private static final HashMap<String, UserResponse> username2resCache = new HashMap<>();

    public static class UserResponse {
        public static final UserResponse NONE = new UserResponse("469433051ddb4db7985d9242acc77a3e", " ");

        public final String id;
        public final String name;

        public UserResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getUUID() {
            return UUID.fromString(id.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
        }

        @Override
        public String toString() {
            return String.format("{name: %s, id: %s}", name, id);
        }
    }

    public static @NotNull UserResponse getUUIDFromUsername(String username) {
        if (username2resCache.get(username) != null) {
            return username2resCache.get(username);
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
                UserResponse response = new GsonBuilder().create().fromJson(rawJson, UserResponse.class);
                username2resCache.put(username, response);
                return response;
            }
            return UserResponse.NONE;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}