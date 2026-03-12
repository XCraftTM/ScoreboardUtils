package de.xcrafttm.scoreboardUtils.configs;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ScoreboardConfig extends OkaeriConfig {

    private String title = "<gold><bold>Your Server Name</bold></bold>";
    private List<String> lines = Arrays.asList(
            "<gray><st>--------------------------</st>",
            " <green>Name<gray>: <gold>%player_displayname%",
            "<gray><st>--------------------------</st>"
    );
}
