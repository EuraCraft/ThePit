package fr.maximouz.thepit.tag;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private static final TagManager INSTANCE = new TagManager();

    private final List<Tag> tags;

    public TagManager() {
        this.tags = new ArrayList<>();
    }

    public static TagManager getInstance() {
        return INSTANCE;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Tag getTag(Player player) {
        return tags.stream().filter(tag -> tag.getPlayer().getUniqueId() == player.getUniqueId()).findAny().orElse(null);
    }

}
