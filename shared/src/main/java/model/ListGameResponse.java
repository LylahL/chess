package model;

import java.util.HashSet;

public record ListGameResponse(HashSet<GameData> games) {
}
