package fr.maximouz.thepit.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.inventory.meta.SkullMeta;

public class SkinUtils {

    public static void applyGoldenHead(SkullMeta meta) {
        GameProfile gameProfile = new GameProfile(null, "jmql");
        PropertyMap map = gameProfile.getProperties();
        map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjgzMzIwMzc5NSwKICAicHJvZmlsZUlkIiA6ICJlZmY5NThkOWRkMjg0N2RlODJhMWQxYjllNTdmYTBkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJqbXFsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyMWZmZjA0NTM3MDk4MmU0ZWMxNTBlNTZmN2MzYzE0ODZhNWM5YzRmODE1MGU3NjI4NzJkZmU4NWFmNWMzNDgiCiAgICB9CiAgfQp9", "PFLTKm+IKsG2/8ycPSOVuehknHfXUiEehDRejHhiCctB3aA0f4dXr86/h5OTKoLEGN6o9e9YgEoZT6ikd+VlajZ+F06XVK/26jrWeFOmMiTdhPq+1UZfK3zDKrxU8Uhq3i6QORt7MbrETKCsYPLN3CBgBkniPBP0i9c1hYmgEfe1T7KySVn7VIWRn6JwxXwboZG3gvZIdQUCgTG5zTiXJ89v4gd7Vi8YFZsKg+JoJpC5U1qOPyYVKumNZAWjM0qF0cKEjhj5oGLcvH8VS4rgCQ7Ps+OKjYECNEKoBRY0iOJ39CPJoPO5ODhKG+v7dtTQ9UuECtth9M3nmy1jv+G1+1wGA54aaLtV9mBsKotosrxkyE+bRt5xLP6A+0WYjqex9agWf90pd2j3j09vJcD0c0hTQH3Kmuf7daW5lfdp8j+xD8CbKH8dkBHZxANAzzKJsILp2mOMUgaslzgF76g61RBaX9hOupvG5WvAAwEy+RBNAEx6FRE+34j6+M6npDEXXX8H16YaGP4LhLEYXVYaUzfLOSMrt8T53k6r3u8+stpzl+RMs5mksPK9LRaBzibdneY5JmMcqWf7zi27maJ5BoHIN0rEEQ4XkXigY4Z/151bbXGpRFh9wRKbWnZGB1ccOObwRWa98AVV9riWXrtl8Ol2t1BihRiSoraeycIvBTA="));

        Reflection.setField(Reflection.getField(meta.getClass(), "profile"), meta, gameProfile);
    }

    public static void applyQuestionMarkHead(SkullMeta meta) {
        GameProfile gameProfile = new GameProfile(null, "_butterscotch");
        PropertyMap map = gameProfile.getProperties();
        map.put("textures", new Property("textures", "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODBjYmRkNGY0NTRmM2U4MzU5YmNjMmI0Y2I4Njk1NWYwZDZkNmM1OGY5Y2E0ZDNjMGI2ZDkzNjRkMTAwYTljNiJ9fX0===", "PFLTKm+IKsG2/8ycPSOVuehknHfXUiEehDRejHhiCctB3aA0f4dXr86/h5OTKoLEGN6o9e9YgEoZT6ikd+VlajZ+F06XVK/26jrWeFOmMiTdhPq+1UZfK3zDKrxU8Uhq3i6QORt7MbrETKCsYPLN3CBgBkniPBP0i9c1hYmgEfe1T7KySVn7VIWRn6JwxXwboZG3gvZIdQUCgTG5zTiXJ89v4gd7Vi8YFZsKg+JoJpC5U1qOPyYVKumNZAWjM0qF0cKEjhj5oGLcvH8VS4rgCQ7Ps+OKjYECNEKoBRY0iOJ39CPJoPO5ODhKG+v7dtTQ9UuECtth9M3nmy1jv+G1+1wGA54aaLtV9mBsKotosrxkyE+bRt5xLP6A+0WYjqex9agWf90pd2j3j09vJcD0c0hTQH3Kmuf7daW5lfdp8j+xD8CbKH8dkBHZxANAzzKJsILp2mOMUgaslzgF76g61RBaX9hOupvG5WvAAwEy+RBNAEx6FRE+34j6+M6npDEXXX8H16YaGP4LhLEYXVYaUzfLOSMrt8T53k6r3u8+stpzl+RMs5mksPK9LRaBzibdneY5JmMcqWf7zi27maJ5BoHIN0rEEQ4XkXigY4Z/151bbXGpRFh9wRKbWnZGB1ccOObwRWa98AVV9riWXrtl8Ol2t1BihRiSoraeycIvBTA="));

        Reflection.setField(Reflection.getField(meta.getClass(), "profile"), meta, gameProfile);
    }

}
